package com.food_delivery.service;

import java.util.List;
import java.util.Optional;

import com.food_delivery.exception.CommonException;
import com.food_delivery.model.CartInfo;
import com.food_delivery.model.CartItemInfo;

public interface CartService {
    List<CartInfo> getCartsByUser(double centerLat, double centerLng);

    Optional<CartInfo> getCartsByUserAndRest(String restaurantId);

    List<CartItemInfo> getCartItemsByCartId(String cartId) throws CommonException;

    void addCartItem(String restaurantId, String productId, int quantity) throws CommonException;

    void removeCartItem(String cartId, String productId) throws CommonException;
}
