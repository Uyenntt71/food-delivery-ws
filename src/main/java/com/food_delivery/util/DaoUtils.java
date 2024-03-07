package com.food_delivery.util;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.function.Function;

import com.food_delivery.model.AddressInfo;
import com.food_delivery.model.CartItemInfo;
import com.food_delivery.model.CartInfo;
import com.food_delivery.model.Category;
import com.food_delivery.model.CustomerInfo;
import com.food_delivery.model.ProductInfo;
import com.food_delivery.model.Restaurant;
import com.food_delivery.model.UserSession;
import com.food_delivery.model.VoucherInfo;

import io.vavr.control.Try;

public class DaoUtils {

    public static Function<ResultSet, Optional<CustomerInfo>> extractCustomer = rs -> {
        CustomerInfo out = Try.of(() -> {
            CustomerInfo ci = new CustomerInfo();
            ci.setPhoneNumber(rs.getString("phone_number"));
            ci.setCustomerId(rs.getString("id"));
            ci.setName(rs.getString("name"));
            ci.setPassword(rs.getString("password"));
            ci.setEmail(rs.getString("email"));

            return ci;
        }).getOrNull();

        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<UserSession>> extractUserSession = rs -> {
        UserSession out = Try.of(() -> {
            UserSession userSession = new UserSession();
            userSession.setId(rs.getString("id"));
            userSession.setBrand(rs.getString("brand"));
            userSession.setCertificateKey(rs.getString("certificate_key"));
            userSession.setActive(rs.getBoolean("active"));
            userSession.setToken(rs.getString("token"));
            userSession.setRefreshToken(rs.getString("refresh_token"));
            userSession.setModel(rs.getString("model"));
            userSession.setCustomerId(rs.getString("customer_id"));
            userSession.setOs(rs.getString("os"));
            userSession.setOsVersion(rs.getString("os_version"));
            userSession.setDeviceName(rs.getString("device_name"));
            userSession.setLastActive(rs.getTimestamp("last_active").getTime());
            return userSession;
        }).getOrNull();
        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<Category>> extractCategory = rs -> {
        Category out = Try.of(() -> {
            Category cat = new Category();
            cat.setId(rs.getString("id"));
            cat.setCategoryName(rs.getString("category_name"));

            return cat;
        }).getOrNull();

        return Optional.ofNullable(out);
    };


    public static Function<ResultSet, Optional<Integer>> extractNumAddress = rs -> {
        Integer out = Try.of(() -> rs.getInt("count")).getOrNull();
        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<Restaurant>> extractRestaurant = rs -> {
        Restaurant out = Try.of(() -> {
            Restaurant rest = new Restaurant();
            rest.setId(rs.getString("id"));
            rest.setName(rs.getString("name"));
            rest.setPhoneNumber(rs.getString("phone_number"));
            rest.setAddress(rs.getString("address"));
            rest.setLat(rs.getDouble("lat"));
            rest.setLng(rs.getDouble("lng"));
            rest.setDistanceToCustomer(rs.getDouble("distance"));
            return rest;
        }).getOrNull();

        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<AddressInfo>> extractAddress = rs -> {
        AddressInfo out = Try.of(() -> {
            AddressInfo add = new AddressInfo();
            add.setId(rs.getString("id"));
            add.setName(rs.getString("name"));
            add.setAddress(rs.getString("address"));
            add.setLat(rs.getDouble("lat"));
            add.setLng(rs.getDouble("lng"));
            return add;
        }).getOrNull();

        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<VoucherInfo>> extractVoucher = rs -> {
        VoucherInfo out = Try.of(() -> {
            VoucherInfo vou = new VoucherInfo();
            vou.setId(rs.getString("id"));
            vou.setName(rs.getString("name"));

            return vou;
        }).getOrNull();

        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<CartInfo>> extractCartInfo = rs -> {
        CartInfo out = Try.of(() -> {
            CartInfo cart = new CartInfo();
            cart.setId(rs.getString("id"));
            cart.setName(rs.getString("name"));
            cart.setNumItems(rs.getInt("count"));
            cart.setDistance(rs.getDouble("distance"));
            cart.setTotalValue(rs.getLong("total"));

            return cart;
        }).getOrNull();

        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<CartItemInfo>> extractCartItemInfo = rs -> {
        CartItemInfo out = Try.of(() -> {
            CartItemInfo cartItemInfo = new CartItemInfo();
            cartItemInfo.setId(rs.getString("id"));
            cartItemInfo.setProductId(rs.getString("product_id"));
            cartItemInfo.setName(rs.getString("name"));
            cartItemInfo.setPrice(rs.getLong("price"));
            cartItemInfo.setQuantity(rs.getInt("num_items"));
            return cartItemInfo;
        }).getOrNull();

        return Optional.ofNullable(out);
    };

    public static Function<ResultSet, Optional<ProductInfo>> extractProductInfo = rs -> {
        ProductInfo out = Try.of(() -> {
            ProductInfo productInfo = new ProductInfo();
            productInfo.setId(rs.getString("id"));
            productInfo.setName(rs.getString("name"));
            productInfo.setPrice(rs.getLong("price"));
            return productInfo;
        }).getOrNull();

        return Optional.ofNullable(out);
    };

}
