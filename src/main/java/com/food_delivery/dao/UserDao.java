package com.food_delivery.dao;

import static com.food_delivery.dao.Helper.execute;
import static com.food_delivery.dao.Helper.select;
import static com.food_delivery.util.CommonUtils.isPhoneNumber;
import static com.food_delivery.util.CommonUtils.normalizePhoneNumber;
import static com.food_delivery.util.DaoUtils.extractAddress;
import static com.food_delivery.util.DaoUtils.extractCustomer;
import static com.food_delivery.util.DaoUtils.extractNumAddress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.food_delivery.conf.AppProps;
import com.food_delivery.model.AddressInfo;
import com.food_delivery.model.CustomerInfo;
import com.food_delivery.model.form.AddressForm;

@Repository
public class UserDao {

    @Autowired
    AppProps appProps;

    @Autowired
    @Qualifier("fdDatasource")
    private DataSource fdDatasource;

    public Optional<CustomerInfo> saveCustomer(CustomerInfo customer) {
        final String sql = "INSERT INTO " + appProps.fdCustomerTable() + "\n" +
                           "(id, email, phone_number, name, password, photo_bin)\n" +
                           "VALUES (?, ?, ?, ?, ?, ?)";

        int inserted = execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(customer.getCustomerId()));
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhoneNumber());
            preparedStatement.setString(4, customer.getName());
            preparedStatement.setString(5, customer.getPassword());
            preparedStatement.setObject(6, customer.getPhoto());
        });

        return inserted > 0 ? Optional.of(customer) : Optional.empty();
    }

    public int updatePassword(String customerId, String password) {
        final String sql = "UPDATE " + appProps.fdCustomerTable() + "\n" +
                           "SET password = ? WHERE id = ?\n";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setString(1, password);
            preparedStatement.setObject(2, UUID.fromString(customerId));
        });
    }


    public Optional<CustomerInfo> getCustomer(String email, String phone) {
        if (isPhoneNumber(phone)) {phone = normalizePhoneNumber(phone);}

        final String sql = "SELECT * FROM " + appProps.fdCustomerTable() + "\n" +
                           "WHERE email = ? OR phone_number = ?";

        String finalPhone = phone;
        List<CustomerInfo> cis = select(fdDatasource, sql, extractCustomer, preparedStatement -> {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, finalPhone);
        });

        return !cis.isEmpty() ? Optional.of(cis.get(0)) : Optional.empty();
    }

    public int addCustomerAddress(String customerId, String name, String address, double lat, double lng) {
        final String sql = "INSERT INTO " + appProps.customerAddressTable() + "\n" +
                           "(name, address, lat, lng, customer_id) VALUES\n"
                           + "(?, ?, ?, ?, ?)";

        return execute(fdDatasource, sql, preparedStatement -> {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setDouble(3, lat);
            preparedStatement.setDouble(4, lng);
            preparedStatement.setObject(5, UUID.fromString(customerId));
        });
    }

    public int countCustomerAddress(String customerId) {
        final String sql = "SELECT COUNT(*) \n"
                           + "FROM " + appProps.customerAddressTable() + " ca\n" +
                           "WHERE ca.customer_id = ?";

        List<Integer> totalList =  select(fdDatasource, sql, extractNumAddress, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(customerId));
        });
        Optional<Integer> total = totalList.stream().findFirst();
        return total.orElse(0);
    }

    public List<AddressInfo> getAddressesByCustomerId(String customerId) {
        final String sql = "SELECT * \n"
                           + "FROM " + appProps.customerAddressTable() + " ca\n" +
                           "WHERE ca.customer_id = ?";

        return select(fdDatasource, sql, extractAddress, preparedStatement -> {
            preparedStatement.setObject(1, UUID.fromString(customerId));
        });
    }
}
