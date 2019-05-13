package com.kyunghwan.Shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ShopController {

    @Autowired
    List<ShopInterfaceRepository> repositories;

    public void printBean(){
        for (ShopInterfaceRepository repository : repositories){
            System.out.println(repository.getClass());
        }
    }
}
