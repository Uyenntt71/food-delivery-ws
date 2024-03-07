package com.food_delivery.service;

import static com.food_delivery.util.CommonUtils.generateRandomUuidAsString;
import static com.food_delivery.util.CommonUtils.getCustomerId;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food_delivery.constant.ErrorDictionary;
import com.food_delivery.dao.CartDao;
import com.food_delivery.exception.CommonException;
import com.food_delivery.exception.ErrorCode;
import com.food_delivery.model.CartInfo;
import com.food_delivery.model.CartItemInfo;

@Service
public class CartServiceImpl implements CartService{
    public static final String PERMISSION_DENIED = "Permission denied";
    private final String FAILED_TO_ADD_CART = "Failed to add cart";
    private final String FAILED_TO_ADD_CART_ITEM = "Failed to add cart item";

    @Autowired
    CartDao cartDao;

    @Override
    public List<CartInfo> getCartsByUser(double centerLat, double centerLng) {
        String userId = getCustomerId();
        return cartDao.getCartsByUserId(userId, centerLat, centerLng);
    }

    @Override
    public Optional<CartInfo> getCartsByUserAndRest(String restaurantId) {
        String userId = getCustomerId();
        return cartDao.getCartsByUserIdAndRestId(userId, restaurantId);
    }

    @Override
    public List<CartItemInfo> getCartItemsByCartId(String cartId) throws CommonException {
        String userId = getCustomerId();
        validateCartId(userId, cartId);
        return cartDao.getCartDetailByCartId(cartId);
    }

    @Override
    public void addCartItem(String restaurantId, String productId, int quantity) throws CommonException{
        String userId = getCustomerId();
        Optional<CartInfo> cartInfo = cartDao.getCartsByUserIdAndRestId(userId, restaurantId);
        if (cartInfo.isPresent()) {
            String cartId = cartInfo.get().getId();
            cartDao.addCartItem(cartId, productId, quantity);
        } else {
            createCartAndAddItem(restaurantId, productId, quantity, userId);
        }
    }

    @Override
    public void removeCartItem(String cartId, String productId) throws CommonException {
        String userId = getCustomerId();
        validateCartId(userId, cartId);

        cartDao.removeCartItem(cartId, productId);
    }

    private void validateCartId(String userId, String cartId) throws CommonException {
        Optional<CartInfo> cartInfoOpt = cartDao.getCartsByUserIdAndCartId(userId, cartId);
        if (!cartInfoOpt.isPresent()) {
            throw new CommonException(PERMISSION_DENIED,
                                      ErrorDictionary.CART_PERMISSION_DENIED,
                                      ErrorCode.PERMISSION_DENIED);
        }
    }

    private void createCartAndAddItem(String restaurantId, String productId, int quantity, String userId) throws CommonException {
        String cartId = generateRandomUuidAsString();
        int createCart = cartDao.addCart(cartId, userId, restaurantId);
        if (createCart <= 0) {
            throw new CommonException(FAILED_TO_ADD_CART, ErrorDictionary.FAILED_TO_ADD_NEW_CART, ErrorCode.INVALID_ARGUMENTS);
        }

        int addCartItem = cartDao.addCartItem(cartId, productId, quantity);
        if (addCartItem <= 0) {
            throw new CommonException(FAILED_TO_ADD_CART_ITEM, ErrorDictionary.FAILED_TO_ADD_CART_ITEM, ErrorCode.INVALID_ARGUMENTS);
        }
    }
}
