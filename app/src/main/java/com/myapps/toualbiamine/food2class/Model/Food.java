package com.myapps.toualbiamine.food2class.Model;

public class Food {
    private String name;
    private  String image;
    private String description;
    private String restaurantID;

    public Food() {

    }

    public Food(String name, String image, String description, String restaurantID) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.restaurantID = restaurantID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
}
