package com.myapps.toualbiamine.food2class;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.*;
import com.myapps.toualbiamine.food2class.Application.DatabaseApp;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.Order;
import com.myapps.toualbiamine.food2class.Model.Request;
import com.myapps.toualbiamine.food2class.Model.User;
import com.myapps.toualbiamine.food2class.Providers.IOrderProvider;
import com.myapps.toualbiamine.food2class.Utils.SwipeToDeleteCallback;
import com.myapps.toualbiamine.food2class.ViewHolder.CartAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    DatabaseReference requestsUsers;

    TextView mealSwipeTotal;
    Button placeOrderBtn;

    List<Order> cart = new ArrayList<>();       //For adapter.
    List<Order> cartData = new ArrayList<>();       //For data manipulation.
    CartAdapter adapter;

    @Inject
    IOrderProvider orderProvider;

    Dialog dietaryRestrictionPopup;
    Button placeOrderPopupBtn;
    ImageButton closePopupBtn;
    EditText restrictionEditText;

    int flagCount = 0;          //Put it here because we cannot access it from function to check flag count below.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/restaurant_font.otf");

        //Initialize Firebase.
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        DatabaseApp.component.injectCart(this);

        dietaryRestrictionPopup = new Dialog(this);

        recyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mealSwipeTotal = (TextView) findViewById(R.id.mealSwipeTotal);
        placeOrderBtn = (Button) findViewById(R.id.placeOrderBtn);

        mealSwipeTotal.setTypeface(font);
        placeOrderBtn.setTypeface(font);

        loadCart();


        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartData = orderProvider.getAll();
                if(cartData.isEmpty() == false) {
                    checkUserFlagCount(Common.currentUser.getEmail());      //All other functions are called from here bc asynchronous calls are made by Firebase
                }
                else {
                    Toast.makeText(getApplicationContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private HashMap<String, List<Order>> filterOrdersByRestaurant(List<Order> cartData) {
        HashMap<String, List<Order>> orderRestaurantMap = new HashMap<>();

        for(Order order : cartData) {
            String restaurantID = order.getRestaurantID();
            if(orderRestaurantMap.containsKey(restaurantID)) {
                List<Order> orders = orderRestaurantMap.get(restaurantID);
                orders.add(order);
                orderRestaurantMap.put(restaurantID, orders);
            }
            else {
                List<Order> orders = new ArrayList<>();
                orders.add(order);
                orderRestaurantMap.put(restaurantID, orders);
            }
        }
        return orderRestaurantMap;
    }

    private void placeOrder(List<Order> cartData, String restriction) {
        HashMap<String, List<Order>> ordersByRestaurants = filterOrdersByRestaurant(cartData);

        //We create a new Request for each restaurant the user is ordering from.
        for(String restaurantID : ordersByRestaurants.keySet()) {
            List<Order> foodOrdered = ordersByRestaurants.get(restaurantID);
            Request newRequest = new Request(Common.currentUser.getEmail(), Common.currentUser.getName(), restriction, foodOrdered, restaurantID);
            //DatabaseReference restaurantDB = database.getReference("Requests/"+restaurantID);       //Pushes to the right restaurant store
            requests.push().setValue(newRequest);
           // restaurantDB.push().setValue(newRequest);
        }

        for(Order order : cartData) {
            orderProvider.delete(order);
        }

        loadCart();
        Toast.makeText(getApplicationContext(), "Order submitted!", Toast.LENGTH_SHORT).show();
    }


    private void showDietaryRestrictionPopup(final List<Order> cartData) {
        //Show the popup.
        dietaryRestrictionPopup.setContentView(R.layout.popup_place_order_msg);
        dietaryRestrictionPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dietaryRestrictionPopup.show();

        placeOrderPopupBtn = (Button) dietaryRestrictionPopup.findViewById(R.id.submitBtn);
        closePopupBtn = (ImageButton) dietaryRestrictionPopup.findViewById(R.id.closePopup);
        restrictionEditText = (MaterialEditText) dietaryRestrictionPopup.findViewById(R.id.restrictionEditText);

        closePopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dietaryRestrictionPopup.dismiss();
            }
        });

        placeOrderPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restriction = restrictionEditText.getText().toString();
                placeOrder(cartData, restriction);
                dietaryRestrictionPopup.dismiss();
            }
        });
    }

    private void displayBanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
        builder.setTitle(Common.BAN_TITLE);
        builder.setMessage(Common.BAN_MSG);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkUserFlagCount(String email) {
        DatabaseReference users = database.getReference("Users");
        Log.e("TAG", "email = " + email);
        users.child(email).child("flagCount").addListenerForSingleValueEvent(new ValueEventListener() {      //This retrieves the value flagCount for the child with the key email
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("TAG", "Inside Listener, flagCount = " + dataSnapshot.getValue().toString());
                flagCount = Integer.parseInt(dataSnapshot.getValue().toString());
                if(flagCount < 3) {
                    showDietaryRestrictionPopup(cartData);
                }
                else {
                    displayBanDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void loadCart() {
        cart = orderProvider.getAll();
        adapter = new CartAdapter(cart, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

}
