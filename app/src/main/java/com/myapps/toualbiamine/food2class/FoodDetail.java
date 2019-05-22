package com.myapps.toualbiamine.food2class;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.*;
import com.myapps.toualbiamine.food2class.Model.Food;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //Initialize Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Food");

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

    }

    private void showFoodDetail (String foodID) {

        //foodID -> passed from Intent when user clicks on a specific choice from menu. Returns 01, 02, 03...
        //Get the table of data assigned to this ID -> returns description, image, name, restaurantID.
        DatabaseReference foodIDTable = foods.child(foodID);

        foodIDTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Food currentFood = dataSnapshot.getValue(Food.class);

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
