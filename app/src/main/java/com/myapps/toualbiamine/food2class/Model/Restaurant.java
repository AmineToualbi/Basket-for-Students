package com.myapps.toualbiamine.food2class.Model;

public class Restaurant {

    private String name;
    private String image;

    public Restaurant() { }

    public Restaurant(String name, String image) {
        this.name = name;
        this.image = image;
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

}
