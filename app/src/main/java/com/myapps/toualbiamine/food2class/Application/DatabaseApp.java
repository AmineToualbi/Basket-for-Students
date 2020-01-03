package com.myapps.toualbiamine.food2class.Application;

import android.app.Application;
import com.myapps.toualbiamine.food2class.Application.DependencyModules.ContextModule;

public class DatabaseApp extends Application {

    public static DatabaseAppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        ContextModule contextModule = new ContextModule(getApplicationContext());

        component = DaggerDatabaseAppComponent
                .builder()
                .contextModule(
                        contextModule
                )
                .build();
    }

}
