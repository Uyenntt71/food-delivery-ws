package com.food_delivery.dao;

import static com.food_delivery.dao.Helper.select;
import static com.food_delivery.util.DaoUtils.extractCategory;
import static com.food_delivery.util.DaoUtils.extractRestaurant;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.food_delivery.conf.AppProps;
import com.food_delivery.model.Category;
import com.food_delivery.model.Restaurant;

@Repository
public class CategoryDao {
    @Autowired
    AppProps appProps;

    @Autowired
    @Qualifier("fdDatasource")
    private DataSource fdDatasource;

    public List<Category> getCategory(int limit, int offset) {
        final String sql = "SELECT * FROM " + appProps.categoryTable() + "\n"
                           + "LIMIT ?\n"
                           + "OFFSET ?\n";

        return select(fdDatasource, sql, extractCategory, preparedStatement -> {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
        });
    }

    public List<Restaurant> getRestaurantByCategory(String categoryId, int limit, int offset, double centerLat, double centerLng) {
        final String sql = "SELECT r.id, r.\"name\", r.phone_number, r.address, r.lat, r.lng,\n"
                           + "ST_DISTANCE(ST_MAKEPOINT(r.lat, r.lng)::geography, ST_MAKEPOINT(?, ?)::geography) AS distance\n"
                           + "FROM " + appProps.restaurantTable() + " r\n"
                           + "JOIN " + appProps.restCatMappingTable() + " rcm ON r.id = rcm.restaurant_id\n"
                           + "WHERE rcm.category_id = ?\n"
                           + "ORDER BY distance\n"
                           + "LIMIT ?\n"
                           + "OFFSET ?\n";

        return select(fdDatasource, sql, extractRestaurant, preparedStatement -> {
            preparedStatement.setDouble(1, centerLat);
            preparedStatement.setDouble(2, centerLng);
            preparedStatement.setObject(3, UUID.fromString(categoryId));
            preparedStatement.setInt(4, limit);
            preparedStatement.setInt(5, offset);
        });
    }

}
