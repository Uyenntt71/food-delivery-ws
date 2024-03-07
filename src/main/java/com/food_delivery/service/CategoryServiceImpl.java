package com.food_delivery.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food_delivery.dao.CategoryDao;
import com.food_delivery.model.Category;
import com.food_delivery.model.Restaurant;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDao productDao;

    public List<Category> getCategory(int limit, int offset) {
        return productDao.getCategory(limit, offset);
    }

    public List<Restaurant> getRestaurantByCategory(String categoryId, int limit, int offset, double centerLat, double centerLng) {
        return productDao.getRestaurantByCategory(categoryId, limit, offset, centerLat, centerLng);
    }
}
