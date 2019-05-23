package com.myapps.toualbiamine.food2class.Providers;

import com.myapps.toualbiamine.food2class.Database.Daos.OrderDao;
import com.myapps.toualbiamine.food2class.Model.Order;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton          //Only 1 instance of this OrderProvider in life of app.
public class OrderProvider implements IOrderProvider {

    OrderDao orderDao;

    @Inject
    public OrderProvider(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public void save(Order order) {
        orderDao.insert(order);
    }

    @Override
    public void update(Order order) {
        orderDao.update(order);
    }

    @Override
    public void delete(Order order) {
        orderDao.delete(order);
    }

}
