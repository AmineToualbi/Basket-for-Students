package com.myapps.toualbiamine.food2class.Interface;

import android.view.View;

//Specialized ClickListener for when the user clicks on an item in a RecyclerView.
public interface ItemClickListener {

    public void onClick(View view, int position, boolean isLongClick);
}
