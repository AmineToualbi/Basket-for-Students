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


//Adapter is needed to populate our views. In the other parts of the app, we use FirebaseUI bc we retrieve everything from Firebase.
//Here, we retrieve data from SQLite.
//Adapter<X> contains the view that we will populate.
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    @Inject
    OrderProvider orderProvider;
    private List<Order> cartData;

    private List<Order> listData;
    private Context context;
    TextView mealSwipePriceTextView;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
        mealSwipePriceTextView = (TextView) ((Activity)context).findViewById(R.id.mealSwipeTotal);
        DatabaseApp.component.injectCartAdapter(this);
        cartData = orderProvider.getAll();



    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout, viewGroup, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder cartViewHolder, int i) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(i).getQuantity(), R.color.actionBarColor);
        cartViewHolder.cartItemImgCount.setImageDrawable(drawable);
        cartViewHolder.cartItemName.setText(listData.get(i).getMenuName());
        mealSwipePriceTextView.setText(getSwipePrice() + " Meal Swipe(s)");
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void deleteItem(int position) {
        cartData = orderProvider.getAll();
        orderProvider.delete(cartData.get(position));
        cartData.remove(position);
        listData.remove(position);
        notifyItemRemoved(position);
        mealSwipePriceTextView.setText(getSwipePrice() + " Meal Swipe(s)");
    }

    public Context getContext() {
        return context;
    }

    private int getSwipePrice() {
        int swipePrice = 0;
        for(Order order : cartData) {
            swipePrice += Integer.parseInt(order.getQuantity());
        }
        return swipePrice;
    }

}

 class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartItemName;
    public ImageView cartItemImgCount;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull final View itemView) {
        super(itemView);
        cartItemName = (TextView) itemView.findViewById(R.id.cartItemName);
        cartItemImgCount = (ImageView) itemView.findViewById(R.id.cartItemImgCount);

        Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/restaurant_font.otf");
        cartItemName.setTypeface(font);
    }

    @Override
    public void onClick(View v) { }

    public void setCartItemName(TextView cartItemName) {
        this.cartItemName = cartItemName;
    }

}


