package com.myapps.toualbiamine.food2class;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapps.toualbiamine.food2class.Application.DatabaseApp;
import com.myapps.toualbiamine.food2class.Model.Order;
import com.myapps.toualbiamine.food2class.Providers.IOrderProvider;
import com.myapps.toualbiamine.food2class.ViewHolder.CartAdapter;

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

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Inject
    IOrderProvider orderProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Initialize Firebase.
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        DatabaseApp.component.injectCart(this);

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

                List<Order> cartData = orderProvider.getAll();

                for(Order order : cartData) {
                    orderProvider.delete(order);
                }

                loadCart();

                Toast.makeText(getApplicationContext(), "Order submitted!", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void loadCart() {

        cart = orderProvider.getAll();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        int swipePrice = 0;
        for(Order order : cart) {
            swipePrice += Integer.parseInt(order.getQuantity());
        }

        mealSwipeTotal.setText(swipePrice + " Meal Swipe(s)");

    }
}
