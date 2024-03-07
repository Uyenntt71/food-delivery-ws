package com.food_delivery.dao;

import static com.food_delivery.dao.Helper.execute;

import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.food_delivery.conf.AppProps;

public class OrderDao {
    @Autowired
    AppProps appProps;

    @Autowired
    @Qualifier("fdDatasource")
    private DataSource fdDatasource;

    public int createOrder(String customerId, long orderDate, long shipDate, String shipAddress, long totalPrice, String status) {
        final String sql = "INSERT INTO " + appProps.orderTable() + " (product_id, cart_id, quantity)\n"
                           + "VALUES (?, ?, ?)\n";

        return execute(fdDatasource, sql, preparedStatement -> {

        });
    }
}
