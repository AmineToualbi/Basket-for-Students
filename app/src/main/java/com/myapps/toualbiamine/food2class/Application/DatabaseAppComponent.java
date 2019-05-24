package com.myapps.toualbiamine.food2class.Application;


import com.myapps.toualbiamine.food2class.Application.DependencyModules.ProviderModule;
import com.myapps.toualbiamine.food2class.Cart;
import com.myapps.toualbiamine.food2class.FoodDetail;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ProviderModule.class})
public interface DatabaseAppComponent {

    public void injectFoodDetail(FoodDetail foodDetail);
    public void injectCart(Cart cart);
}
