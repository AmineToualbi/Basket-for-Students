package com.myapps.toualbiamine.food2class.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.myapps.toualbiamine.food2class.Database.Daos.OrderDao;
import com.myapps.toualbiamine.food2class.Model.Order;

@Database(
        entities = {Order.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract OrderDao orderDao();




}
