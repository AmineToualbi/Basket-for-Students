package com.myapps.toualbiamine.food2class.Database.Daos;

import android.arch.persistence.room.*;
import android.arch.persistence.room.OnConflictStrategy.*;
import com.myapps.toualbiamine.food2class.Model.Order;

import java.util.List;

@Dao
public interface OrderDao {     //Data Access Object.

    @Query("SELECT * FROM orders")
    public List<Order> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Order order);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    public void update(Order order);

    @Delete
    public void delete(Order order);

}
