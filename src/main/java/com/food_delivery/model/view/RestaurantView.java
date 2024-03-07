package com.food_delivery.model.view;

import java.util.List;

import com.food_delivery.model.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantView {
    private int total;

    private List<Restaurant> restaurantList;
}
