package com.myapps.toualbiamine.food2class;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapps.toualbiamine.food2class.Application.DatabaseApp;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.Order;
import com.myapps.toualbiamine.food2class.Model.Request;
import com.myapps.toualbiamine.food2class.Providers.IOrderProvider;
import com.myapps.toualbiamine.food2class.Utils.SwipeToDeleteCallback;
import com.myapps.toualbiamine.food2class.ViewHolder.CartAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        loadCart();

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartData = orderProvider.getAll();
                if(cartData.isEmpty() == false) {
                    showDietaryRestrictionPopup(cartData);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Your cart is empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void placeOrder(List<Order> cartData, String restriction) {
        Request newRequest = new Request(Common.currentUser.getEmail(), Common.currentUser.getName(),
                restriction, cartData);

        //Submit Order -> use CurrentSystemMillis as key!
        requests.child(String.valueOf(System.currentTimeMillis())).setValue(newRequest);

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


    public void loadCart() {
        cart = orderProvider.getAll();
        adapter = new CartAdapter(cart, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
    }

}
