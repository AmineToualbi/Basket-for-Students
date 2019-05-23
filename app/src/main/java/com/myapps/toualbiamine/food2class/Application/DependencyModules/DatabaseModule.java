package com.myapps.toualbiamine.food2class.Application.DependencyModules;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.myapps.toualbiamine.food2class.Database.AppDatabase;
import com.myapps.toualbiamine.food2class.Database.Daos.OrderDao;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module(includes = {ContextModule.class})
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase appDatabase(Context context) {

        return Room.databaseBuilder(context, AppDatabase.class, "OrdersDB.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

    }

    @Provides
    public OrderDao orderDao(AppDatabase database) {
        return database.orderDao();
    }


}
