package com.food_delivery.dao;

import static com.food_delivery.dao.Helper.select;
import static com.food_delivery.util.DaoUtils.extractCartInfo;
import static com.food_delivery.util.DaoUtils.extractProductInfo;

import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.food_delivery.conf.AppProps;
import com.food_delivery.model.CartInfo;
import com.food_delivery.model.ProductInfo;

@Repository
public class ProductDao {
    @Autowired
    AppProps appProps;

    @Autowired
    @Qualifier("fdDatasource")
    private DataSource fdDatasource;

    public Optional<ProductInfo> getProductByRestaurantAndProductId(String restaurantId, String productId) {
        final String sql = "SELECT p.id, p.name, p.price\n"
                           + "FROM " + appProps.productTable() + " p\n"
                           + "JOIN " + appProps.menuTable() + " m ON m.id = p.menu_id\n"
                           + "JOIN " + appProps.restaurantTable() + " r ON r.id = m.restaurant_id\n"
                           + "WHERE r.id = ?"
                           + "  AND p.id = ?\n";
        return select(fdDatasource, sql, extractProductInfo, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(restaurantId));
            preparedStatement.setObject(2, UUID.fromString(productId));
        }).stream().findFirst();
    }
}
