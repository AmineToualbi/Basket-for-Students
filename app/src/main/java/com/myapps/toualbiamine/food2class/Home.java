package com.myapps.toualbiamine.food2class;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Interface.ItemClickListener;
import com.myapps.toualbiamine.food2class.Model.Restaurant;
import com.myapps.toualbiamine.food2class.ViewHolder.MenuViewHolder;
import com.squareup.picasso.Picasso;
import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference restaurant;

    TextView userName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

     FirebaseRecyclerAdapter<Restaurant, MenuViewHolder> adapter;

     String TAG = "HomeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        Log.d(TAG, "Logged in as " + Common.currentUser.getEmail());

        //Initialize Firebase
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("Restaurants");

        //Initialize Paper => library to use to save key-value pairs on phone storage = easier than SharedPreferences.
        Paper.init(this);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), Cart.class);
                startActivity(cart);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //Set name for user.
        View headerView = navigationView.getHeaderView(0);
        userName = (TextView) headerView.findViewById(R.id.userName);       //Need the headerView here to avoid crash!
        if(Common.currentUser!=null)
            userName.setText(Common.currentUser.getName());

        //Load the menu & RecyclerView.
        recycler_menu = (RecyclerView) findViewById(R.id.recyclerMenu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

    }

    //FirebaseUI -> https://firebaseopensource.com/projects/firebase/firebaseui-android/database/readme.md/
    private void loadMenu() {

         adapter = new FirebaseRecyclerAdapter<Restaurant, MenuViewHolder>(Restaurant.class,
                R.layout.menu_item, MenuViewHolder.class, restaurant) {

            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Restaurant model, int position) {

                viewHolder.menuName.setText(model.getName());

                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.menuImg);

                final Restaurant restaurantSelected = model;

                //What happens when user clicks on item? Send RestaurantID to new Activity to show its food.
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodList = new Intent(getApplicationContext(), FoodList.class);
                        //RestaurantID = key of the restaurant in our db.
                        foodList.putExtra("RestaurantID", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
            }
        };

        recycler_menu.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_cart) {

            Intent cart = new Intent(getApplicationContext(), Cart.class);
            startActivity(cart);

        } else if (id == R.id.nav_orders) {

            Intent orders = new Intent(getApplicationContext(), OrderStatus.class);
            startActivity(orders);

        } else if (id == R.id.nav_log_out) {

            Paper.book().destroy();     //Destroy entire book of entries.
            FirebaseAuth.getInstance().signOut();
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            //Flags to clean previous activity & avoid having previously entered credentials!
           /* signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);*/
            startActivity(mainActivity);
            finish();

        } else if (id == R.id.nav_share) {

            //TODO - Send link of app to ur friends!

        } else if (id == R.id.nav_support) {

            Intent support = new Intent(Intent.ACTION_SENDTO);
            support.setData(Uri.parse("mailto:"));
            support.putExtra(Intent.EXTRA_EMAIL, "amtoualbi@gmail.com");
            support.putExtra(Intent.EXTRA_SUBJECT, "[Food2Class] Support Request");

            if(support.resolveActivity(getPackageManager()) != null) {
                startActivity(support);
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
