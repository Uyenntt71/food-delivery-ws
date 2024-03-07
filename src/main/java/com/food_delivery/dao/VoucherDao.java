package com.food_delivery.dao;

import static com.food_delivery.dao.Helper.select;
import static com.food_delivery.util.DaoUtils.extractRestaurant;
import static com.food_delivery.util.DaoUtils.extractVoucher;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.food_delivery.conf.AppProps;
import com.food_delivery.model.Restaurant;
import com.food_delivery.model.VoucherInfo;

@Repository
public class VoucherDao {
    @Autowired
    AppProps appProps;

    @Autowired
    @Qualifier("fdDatasource")
    private DataSource fdDatasource;

    public List<VoucherInfo> getVouchers(int limit, int offset) {
        final String sql = "SELECT * FROM " + appProps.voucherTable() + "\n"
                           + "LIMIT ?\n"
                           + "OFFSET ?\n";

        return select(fdDatasource, sql, extractVoucher, preparedStatement -> {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
        });
    }

    public List<Restaurant> getRestaurantsByVoucher(String voucherId, int limit, int offset, double centerLat, double centerLng) {
        final String sql = "SELECT r.id, r.\"name\", r.phone_number, r.address, r.lat, r.lng,\n"
                           + "ST_DISTANCE(ST_MAKEPOINT(r.lat, r.lng)::geography, ST_MAKEPOINT(?, ?)::geography) AS distance\n"
                           + "FROM " + appProps.restaurantTable() + " r\n"
                           + "JOIN " + appProps.restVouMappingTable() + " rcm ON r.id = rcm.restaurant_id\n"
                           + "WHERE rcm.voucher_id = ?\n"
                           + "ORDER BY distance\n"
                           + "LIMIT ?\n"
                           + "OFFSET ?\n";
        ;

        return select(fdDatasource, sql, extractRestaurant, preparedStatement -> {
            preparedStatement.setDouble(1, centerLat);
            preparedStatement.setDouble(2, centerLng);
            preparedStatement.setObject(3, UUID.fromString(voucherId));
            preparedStatement.setInt(4, limit);
            preparedStatement.setInt(5, offset);
        });
    }
}
