package com.myapps.toualbiamine.food2class.Model;

import java.util.List;

public class Request {

    private String email;
    private String name;
    private List<Order> order;
    private String restriction;
    private String status;

    public Request() { }

    public Request(String email, String name, String restriction, List<Order> order) {
        this.email = email;
        this.name = name;
        this.restriction = restriction;
        this.order = order;
        this.status = "0";          //0: Placed. 1: Ready. 2: Picked up.
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getRestriction() {
        return restriction;
    }
    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Order> getOrder() {
        return order;
    }
    public void setOrder(List<Order> order) {
        this.order = order;
    }

}
