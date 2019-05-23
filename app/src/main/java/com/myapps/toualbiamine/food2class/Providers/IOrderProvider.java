package com.myapps.toualbiamine.food2class.Providers;

import com.myapps.toualbiamine.food2class.Model.Order;

import java.util.List;

public interface IOrderProvider {

    public List<Order> getAll();

    public void save(Order order);

    public void update(Order order);

    public void delete(Order order);
}
