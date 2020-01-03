package com.myapps.toualbiamine.food2class.ViewHolder;

/*Steps to creating & implementing a RecyclerView:
    1. Create an activity that will contain it, i.e.: FoodList.
    2. Create a Card for the items that will be displayed in the list.
    3. Create a ViewHolder following the implementation below.
    4. Do ur thing in FoodList.
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.myapps.toualbiamine.food2class.Interface.ItemClickListener;
import com.myapps.toualbiamine.food2class.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderID;
    public TextView orderStatus;
    public TextView orderMenu;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        orderID = (TextView) itemView.findViewById(R.id.orderID);
        orderStatus = (TextView) itemView.findViewById(R.id.orderStatus);
        orderMenu = (TextView) itemView.findViewById(R.id.orderMenu);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
    //No onClick => otherwise it throws an error.
       //itemClickListener.onClick(v, getAdapterPosition(), false);
    }

}
