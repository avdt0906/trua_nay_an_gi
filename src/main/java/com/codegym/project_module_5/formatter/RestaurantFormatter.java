package com.codegym.project_module_5.formatter;

import com.codegym.project_module_5.model.restaurant_model.Restaurant;
import com.codegym.project_module_5.service.restaurant_service.IRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

@Component
public class RestaurantFormatter implements Formatter<Restaurant> {
    @Autowired
    private IRestaurantService restaurantService;

    @Override
    public Restaurant parse(String text, Locale locale) throws ParseException {
        Optional<Restaurant> restaurant = restaurantService.findById(Long.parseLong(text));
        return restaurant.get();
    }

    @Override
    public String print(Restaurant object, Locale locale) {
        return object.getName();
    }
}
