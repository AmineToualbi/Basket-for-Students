package com.myapps.toualbiamine.food2class.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.myapps.toualbiamine.food2class.Interface.ItemClickListener;
import com.myapps.toualbiamine.food2class.Model.Order;
import com.myapps.toualbiamine.food2class.R;

import java.util.ArrayList;
import java.util.List;


class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartItemName;
    public ImageView cartItemImgCount;
    public ImageButton deleteCartItemBtn;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull final View itemView) {
        super(itemView);

        cartItemName = (TextView) itemView.findViewById(R.id.cartItemName);
        cartItemImgCount = (ImageView) itemView.findViewById(R.id.cartItemImgCount);
//        deleteCartItemBtn = (ImageButton) itemView.findViewById(R.id.deleteCartItemBtn);

        /*deleteCartItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });*/

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
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
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

        final int position = i;

        cartViewHolder.cartItemName.setText(listData.get(i).getmenuName());
//     //   cartViewHolder.deleteCartItemBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(cartViewHolder.itemView.getContext(), "Clicked! " + position , Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
