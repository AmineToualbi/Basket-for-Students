package com.myapps.toualbiamine.food2class.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Orders")
public class Order {

    @PrimaryKey int orderID;
    public String menuID;       //If private => weird bug w Room.
    public String menuName;
    String quantity;

    public Order() {

    }

    public Order(int orderID, String menuID, String menuName, String quantity) {
        this.orderID = orderID;
        this.menuID = menuID;
        this.menuName = menuName;
        this.quantity = quantity;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getmenuID() {
        return menuID;
    }

    public void setmenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getmenuName() {
        return menuName;
    }

    public void setmenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
