package com.myapps.toualbiamine.food2class.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Orders")
public class Order {

    @PrimaryKey int orderID;
    private String menuID;       //If private => weird bug w Room.
    private String restaurantID;
    private String menuName;
    private String quantity;

    public Order() { }

    public Order(int orderID, String menuID, String restaurantID, String menuName, String quantity) {
        this.orderID = orderID;
        this.menuID = menuID;
        this.restaurantID = restaurantID;
        this.menuName = menuName;
        this.quantity = quantity;
    }

    public int getOrderID() {
        return orderID;
    }
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
    public String getMenuID() {
        return menuID;
    }
    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }
    public String getRestaurantID() {
        return restaurantID;
    }
    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
    public String getMenuName() {
        return menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
