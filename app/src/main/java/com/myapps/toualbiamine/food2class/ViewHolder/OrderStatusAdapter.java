package com.myapps.toualbiamine.food2class.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.myapps.toualbiamine.food2class.Application.DatabaseApp;
import com.myapps.toualbiamine.food2class.Interface.ItemClickListener;
import com.myapps.toualbiamine.food2class.Model.Order;
import com.myapps.toualbiamine.food2class.Providers.OrderProvider;
import com.myapps.toualbiamine.food2class.R;

import javax.inject.Inject;
import java.util.List;

class OrderStatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartItemName;
    public ImageView cartItemImgCount;

    private ItemClickListener itemClickListener;

    public OrderStatusViewHolder(@NonNull final View itemView) {
        super(itemView);
        cartItemName = (TextView) itemView.findViewById(R.id.cartItemName);
        cartItemImgCount = (ImageView) itemView.findViewById(R.id.cartItemImgCount);

        Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/restaurant_font.otf");
        cartItemName.setTypeface(font);
    }

    @Override
    public void onClick(View v) {
    }

    public void setCartItemName(TextView cartItemName) {
        this.cartItemName = cartItemName;
    }

}


//Adapter is needed to populate our views. In the other parts of the app, we use FirebaseUI bc we retrieve everything from Firebase.
//Here, we retrieve data from SQLite.
//Adapter<X> contains the view that we will populate.
public class OrderStatusAdapter extends RecyclerView.Adapter<CartViewHolder> {


    private List<Order> listData;
    private Context context;

    public OrderStatusAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public Context getContext() {
        return context;
    }

}
