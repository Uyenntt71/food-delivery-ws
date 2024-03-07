package com.food_delivery.dao;

import static com.food_delivery.dao.Helper.execute;
import static com.food_delivery.dao.Helper.select;
import static com.food_delivery.util.DaoUtils.extractCartItemInfo;
import static com.food_delivery.util.DaoUtils.extractCartInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.food_delivery.conf.AppProps;
import com.food_delivery.model.CartItemInfo;
import com.food_delivery.model.CartInfo;

@Repository
public class CartDao {
    @Autowired
    AppProps appProps;

    @Autowired
    @Qualifier("fdDatasource")
    private DataSource fdDatasource;

    public int addCart(String cartId, String userId, String restaurantId) {
        final String sql = "INSERT INTO " + appProps.cartTable() + " (id, user_id, restaurant_id)\n"
                           + "VALUES (?, ?, ?)\n";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(cartId));
            preparedStatement.setObject(2, UUID.fromString(userId));
            preparedStatement.setObject(3, UUID.fromString(restaurantId));
        });
    }

    public int addCartItem(String cartId, String productId, int quantity) {
        final String sql = "INSERT INTO " + appProps.cartItemTable() + " (product_id, cart_id, quantity)\n"
                           + "VALUES (?, ?, ?)\n";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(productId));
            preparedStatement.setObject(2, UUID.fromString(cartId));
            preparedStatement.setInt(3, quantity);
        });
    }

    public int removeCartItem(String cartId, String productId) {
        final String sql = "DELETE FROM " + appProps.cartItemTable() + " ci\n"
                           + "WHERE ci.cart_id = ? AND ci.product_id = ?\n";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(cartId));
            preparedStatement.setObject(2, UUID.fromString(productId));
        });
    }

    public int updateCartItem(String cartId, String productId, int quantity) {
        final String sql = "UPDATE " + appProps.cartItemTable() + " ci\n"
                           + "SET quantity = ? \n"
                           + "WHERE ci.cart_id = ? AND ci.product_id = ?\n";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setObject(2, UUID.fromString(cartId));
            preparedStatement.setObject(3, UUID.fromString(productId));
        });
    }

    public int removeCart(String cartId) {
        final String sql = "DELETE FROM " + appProps.cartTable() + " c\n"
                           + "WHERE c.cart_id = ?\n";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(cartId));
        });
    }

    public int removeCartItemsByCartId(String cartId) {
        final String sql = "DELETE FROM " + appProps.cartItemTable() + " ci\n"
                           + "WHERE ci.cart_id = ?\n";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(cartId));
        });
    }

    public List<CartInfo> getCartsByUserId(String userId, double centerLat, double centerLng) {
        final String sql = "SELECT c.id, r.name, count(ci.product_id), null as total,\n"
                           + "ST_DISTANCE(ST_MAKEPOINT(r.lat, r.lng)::geography, ST_MAKEPOINT(?, ?)::geography) AS distance\n"
                           + "FROM " + appProps.cartTable() + " c\n"
                           + "JOIN " + appProps.restaurantTable() + " r ON r.id = c.restaurant_id\n"
                           + "JOIN " + appProps.cartItemTable() + " ci ON ci.cart_id = c.id\n"
                           + "WHERE c.user_id = ?\n"
                           + "GROUP BY c.id, r.name, r.lat, r.lng\n"
                           + "ORDER BY distance;";

        return select(fdDatasource, sql, extractCartInfo, preparedStatement -> {
            preparedStatement.setDouble(1, centerLat);
            preparedStatement.setDouble(2, centerLng);
            preparedStatement.setObject(3, UUID.fromString(userId));
        });
    }

    public Optional<CartInfo> getCartsByUserIdAndCartId(String userId, String cartId) {
        final String sql = "SELECT c.id, r.name, null as distance, null as total, null as count\n"
                           + "FROM " + appProps.cartTable() + " c\n"
                           + "JOIN " + appProps.restaurantTable() + " r ON r.id = c.restaurant_id\n"
                           + "WHERE c.user_id = ?"
                           + "  AND c.id = ?\n";
        return select(fdDatasource, sql, extractCartInfo, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(userId));
            preparedStatement.setObject(2, UUID.fromString(cartId));
        }).stream().findFirst();
    }

    public Optional<CartInfo> getCartsByUserIdAndRestId(String userId, String restaurantId) {
        final String sql = "SELECT c.id, count(ci.product_id), sum(ci.quantity * p.price) as total, null as name, null as distance\n"
                           + "FROM " + appProps.cartTable() + " c\n"
                           + "JOIN " + appProps.cartItemTable() + " ci ON ci.cart_id = c.id\n"
                           + "JOIN " + appProps.productTable() + " p ON p.id = ci.product_id\n"
                           + "WHERE c.user_id = ?"
                           + "  AND c.restaurant_id = ?\n"
                           + "GROUP BY c.id\n";
        return select(fdDatasource, sql, extractCartInfo, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(userId));
            preparedStatement.setObject(2, UUID.fromString(restaurantId));
        }).stream().findFirst();
    }

    public List<CartItemInfo> getCartDetailByCartId(String cartId) {
        final String sql = "SELECT ci.id, ci.quantity as num_items, ci.product_id, p.name , p.price\n"
                           + "FROM " + appProps.cartItemTable() + " ci\n"
                           + "JOIN " + appProps.productTable() + " p ON p.id = ci.product_id\n"
                           + "WHERE ci.cart_id = ?\n"
                           + "ORDER BY name;";

        return select(fdDatasource, sql, extractCartItemInfo, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(cartId));
        });
    }


}
