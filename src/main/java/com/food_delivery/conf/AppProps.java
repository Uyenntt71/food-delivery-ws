package com.food_delivery.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.experimental.Accessors;

@Component
@Data
@Accessors(fluent = true)
@Configuration
@EnableConfigurationProperties
public class AppProps {
    @Value("${postgres.fd.pool.size}")
    private int postgreSqlPoolSize;

    /**
     * Config Food Delivery table
     */
    @Value("${postgres.fd.jdbc.url}")
    private String postgresUrl;

    @Value("${postgres.fd.username}")
    private String postgresUser;

    @Value("${postgres.fd.password}")
    private String postgresPwd;

    @Value("${postgres.fd.jdbc.default-schema}")
    private String defaultSchema;

    @Value("${postgres.fd.table.customer}")
    private String fdCustomerTable;

    @Value("${postgres.fd.table.food}")
    private String foodTable;

    @Value("${postgres.fd.table.category}")
    private String categoryTable;

    @Value("${postgres.fd.table.restaurant}")
    private String restaurantTable;

    @Value("${postgres.fd.table.order}")
    private String orderTable;

    @Value("${postgres.fd.table.order-detail}")
    private String orderDetailTable;

    @Value("${postgres.fd.table.user-session}")
    private String userSessionTable;

    @Value("${postgres.fd.table.product}")
    private String productTable;

    @Value("${postgres.fd.table.menu}")
    private String menuTable;

    @Value("${postgres.fd.table.customer-address}")
    private String customerAddressTable;

    @Value("${postgres.fd.table.restaurant-category-mapping}")
    private String restCatMappingTable;

    @Value("${postgres.fd.table.voucher}")
    private String voucherTable;

    @Value("${postgres.fd.table.restaurant-voucher-mapping}")
    private String restVouMappingTable;

    @Value("${postgres.fd.table.cart}")
    private String cartTable;

    @Value("${postgres.fd.table.cart-item}")
    private String cartItemTable;

    /**
    * Redis config
    */
    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private Integer redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    /**
     * Bucket config
     */
    @Value("${param.token.valid-time}")
    private long tokenValidTime;

    @Value("${param.token.signing-key}")
    private String tokenSigningKey;

    @Value("${otp.try-time}")
    private int otpTryTime;

    @Value("${otp.real-otp}")
    private String realOtp;

    @Value("${salt}")
    private String salt;

    @Value("${param.maxAddress}")
    private int maxAddress;

    /**
     * Email config
     */

    @Value("${email.host}")
    private String emailHost;

    @Value("${email.port}")
    private int emailPort;

    @Value("${email.username}")
    private String emailUsername;

    @Value("${email.password}")
    private String emailPassword;

    @Value("${email.body}")
    private String emailBody;

    @Value("${email.subject}")
    private String emailSubject;

    @Value("${email.properties.mail.transport.protocol}")
    private String emailProtocol;

    @Value("${email.properties.mail.smtp.auth}")
    private String emailAuth;

    @Value("${email.properties.mail.smtp.starttls.enable}")
    private String emailStarttls;

    @Value("${email.properties.mail.smtp.starttls.required}")
    private String emailStarttlsRequired;

    @Value("${email.properties.mail.smtp.timeout}")
    private String emailTimeout;

    @Value("${email.properties.mail.smtp.connection-timeout}")
    private String emailConnectionTimeout;
}
