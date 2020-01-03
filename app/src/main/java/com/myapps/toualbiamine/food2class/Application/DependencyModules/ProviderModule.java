package com.myapps.toualbiamine.food2class.Application.DependencyModules;

import com.myapps.toualbiamine.food2class.Providers.IOrderProvider;
import com.myapps.toualbiamine.food2class.Providers.OrderProvider;
import dagger.Binds;
import dagger.Module;

@Module(includes = {DatabaseModule.class})
public abstract class ProviderModule {

    @Binds      //Binds abstract classes & modules.
    public abstract IOrderProvider binOrderProvider(OrderProvider implementation);
}

