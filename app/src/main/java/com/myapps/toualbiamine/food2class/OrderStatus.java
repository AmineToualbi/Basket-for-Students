package com.myapps.toualbiamine.food2class;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.*;
import com.google.firebase.database.core.Path;
import com.myapps.toualbiamine.food2class.Common.Common;
import com.myapps.toualbiamine.food2class.Model.Food;
import com.myapps.toualbiamine.food2class.Model.Order;
import com.myapps.toualbiamine.food2class.Model.Request;
import com.myapps.toualbiamine.food2class.ViewHolder.OrderViewHolder;

import java.util.List;

public class OrderStatus extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    DatabaseReference foods;
    DatabaseReference restaurants;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    String TAG = "OrderStatusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Initialize Firebase.
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        foods = database.getReference("Foods");
        restaurants = database.getReference("Restaurants");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getEmail());

        Log.d(TAG, "LoadOrders() from " + Common.currentUser.getEmail());


    }

    private void loadOrders(String userEmail) {


        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,
                R.layout.order_layout, OrderViewHolder.class,
                //orderByChild -> returns Query where child nodes are ordered by restaurantIDs.
                //equalTo -> return Query of child node with constrained value.
                //Remember to add indexOn in Firebase rules.
                requests.orderByChild("email").equalTo(userEmail)) { //Select * FROM Requests where email = userEmail
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.orderID.setText("#" + adapter.getRef(position).getKey());
                String orderStatus = convertCodeToStatus(model.getStatus());
                viewHolder.orderStatus.setText(orderStatus);

                List<Order> activeOrders = model.getOrder();
                String orderMenu = "";

                for(int i=0; i<activeOrders.size(); i++) {
                    String currentMenu = activeOrders.get(i).getmenuName();
                    String currentQuantity = activeOrders.get(i).getQuantity();
                    //orderMenu += currentMenu + " x" + currentQuantity + " | ";
                    if(i != 0) {
                        orderMenu += "\n";
                    }
                    orderMenu += currentQuantity + " " + currentMenu;
                }

                viewHolder.orderMenu.setText(orderMenu);



            }
        };

        recyclerView.setAdapter(adapter);

    }

    private String convertCodeToStatus(String status) {

        if(status != null && status.equals("0")) return "Placed";
        else if(status != null && status.equals("1")) return "Ready";
        else if (status != null && status.equals("2")) return  "Picked up";
            return "";
    }
}
