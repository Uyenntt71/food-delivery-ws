package com.food_delivery.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food_delivery.dao.ProductDao;
import com.food_delivery.model.ProductInfo;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    ProductDao productDao;

    @Override
    public Optional<ProductInfo> getProductByProductIdAndRestaurantId(String productId, String restaurantId) {
        return productDao.getProductByRestaurantAndProductId(restaurantId, productId);
    }
}
