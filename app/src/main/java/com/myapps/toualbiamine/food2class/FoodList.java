package com.myapps.toualbiamine.food2class;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapps.toualbiamine.food2class.Interface.ItemClickListener;
import com.myapps.toualbiamine.food2class.Model.Food;
import com.myapps.toualbiamine.food2class.ViewHolder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String restaurantID;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    String TAG = "FoodListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Initialize Firebase.
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        //Create RecyclerView.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerFood);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        //Get restaurantID passed in Intent from Home.
        if(getIntent() != null) {
            restaurantID = getIntent().getStringExtra("RestaurantID");
            Log.d(TAG, "Restaurant ID - " + restaurantID);
        }

        Log.d(TAG, "FoodList Key - " + foodList.orderByChild("RestaurantID").equalTo(restaurantID));

        if(!(restaurantID.isEmpty()) && restaurantID != null) {
            loadFoodList(restaurantID);
        }
    }

    //Added this part to the rules in Firebase for faster & safer access to data.
    //"Food": {
    //        ".indexOn":["restaurantID"]
    //      }
    //Use FirebaseUI if you have RecyclerView w ViewHolder to populate it easily!
    private void loadFoodList(String restaurantID) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item, FoodViewHolder.class,
                //orderByChild -> returns Query where child nodes are ordered by restaurantIDs.
                //equalTo -> return Query of child node with constrained value.
                foodList.orderByChild("restaurantID").equalTo(restaurantID))    //Select * FROM Foods where RestaurantID = restaurantID
        {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.foodName.setText(model.getName());

                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.foodImage);

                final Food foodSelected = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(getApplicationContext(), FoodDetail.class);
                        //Send FoodID to new page = menuID.
                        foodDetail.putExtra("FoodID", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        //Set adapter
        Log.d(TAG, "Adapter - " + adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }

}
