package com.myapps.toualbiamine.food2class.Application.DependencyModules;


import android.content.Context;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module         //Tells Dagger to use dis for dependency injection.
public class ContextModule {

    Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context context() {
        return context;
    }



}
