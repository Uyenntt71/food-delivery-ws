package com.food_delivery.service;

import java.util.Optional;

import com.food_delivery.model.ProductInfo;

public interface ProductService {
    Optional<ProductInfo> getProductByProductIdAndRestaurantId(String productId, String restaurantId);
}
