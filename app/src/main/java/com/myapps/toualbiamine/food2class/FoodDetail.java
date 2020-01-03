package com.myapps.toualbiamine.food2class;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.myapps.toualbiamine.food2class.Application.DatabaseApp;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.Food;
import com.myapps.toualbiamine.food2class.Model.Order;
import com.myapps.toualbiamine.food2class.Providers.IOrderProvider;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

public class FoodDetail extends AppCompatActivity {

    TextView foodName;
    TextView foodDescription;
    ImageView foodImage;

    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar foodDetailToolbar;

    FloatingActionButton cartFab;

    ElegantNumberButton quantityBtn;

    FirebaseDatabase database;
    DatabaseReference foods;

    String foodID;

    Food currentFood;

    String TAG = "FoodDetailActivity";

    @Inject
    IOrderProvider orderProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Initialize Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        DatabaseApp.component.injectFoodDetail(this);


        quantityBtn = (ElegantNumberButton) findViewById(R.id.quantityBtn);
        cartFab = (FloatingActionButton) findViewById(R.id.cartFab);

        foodName = (TextView) findViewById(R.id.foodName);
        foodDescription = (TextView) findViewById(R.id.foodDescription);
        foodImage = (ImageView) findViewById(R.id.foodImg);

        foodDetailToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.foodDetailToolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        //Apply the created styles in styles.xml to this collapsingToolbarLayout.
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        //Get FoodID from Intent
        if(getIntent() != null) {
          foodID = getIntent().getStringExtra("FoodID");
        }
        if(!foodID.isEmpty()) {
            showFoodDetail(foodID);
        }

        cartFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Order - " + foodID + " " + currentFood.getName() + " " + quantityBtn.getNumber());

                Order newOrder = new Order(Common.orderID, foodID, currentFood.getName(),
                        quantityBtn.getNumber());

                Common.orderID++;
                Log.d(TAG, "newOrder.foodID = " + newOrder.getMenuID());

                orderProvider.save(newOrder);

                Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();

                final List<Order> orderData = orderProvider.getAll();

                if(orderData == null || orderData.isEmpty()) {
                    showMessage("No Data!");
                }
                else {
                    String jsonData = (new Gson()).toJson(orderData);
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        Log.d(TAG, "jsonArray.length = " + jsonArray.length());
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Log.d(TAG, obj.getInt("orderID") + " " + obj.getString("menuID")
                            + " " + obj.getString("menuName") + " " + obj.getString("quantity") + "\n");
                        }
                    }
                    catch (JSONException e) {
                        Log.d(TAG, "JSONEXCEPTION");
                    }
                    Log.d(TAG, jsonData);
                }
            }
        });
    }


    private void showMessage(String msg) {
        new AlertDialog.Builder(getApplicationContext()).setMessage(msg).create().show();
    }


    private void showFoodDetail (String foodID) {
        //foodID -> passed from Intent when user clicks on a specific choice from menu. Returns 01, 02, 03...
        //Get the table of data assigned to this ID -> returns description, image, name, restaurantID.
        DatabaseReference foodIDTable = foods.child(foodID);

        foodIDTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(currentFood.getImage())
                        .into(foodImage);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                foodDescription.setText(currentFood.getDescription());
                foodName.setText(currentFood.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
