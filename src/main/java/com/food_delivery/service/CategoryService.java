package com.food_delivery.service;

import java.util.List;

import com.food_delivery.model.Category;
import com.food_delivery.model.Restaurant;

public interface CategoryService {
    List<Category> getCategory(int limit, int offset);

    List<Restaurant> getRestaurantByCategory(String categoryId, int limit, int offset, double centerLat, double centerLng);
}
