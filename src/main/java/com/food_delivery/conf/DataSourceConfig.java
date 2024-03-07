package com.food_delivery.conf;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
//@Profile("dev")
public class DataSourceConfig {
    private static final String EMAIL_PROTOCOL = "mail.transport.protocol";
    private static final String EMAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String EMAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String EMAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    private static final String EMAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    private static final String EMAIL_SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";


    @Autowired
    AppProps appProps;

    private DataSource postgreSqlDataSource(String url, String user, String pwd, String db, int poolSize) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pwd);
        config.setPoolName("fd-" + db + "-pool");
        config.setMaximumPoolSize(poolSize);
        config.setMinimumIdle(4);
        config.setIdleTimeout(20000);
        config.setMaxLifetime(30000);

        return new HikariDataSource(config);
    }

    @Bean(value = "fdDatasource")
    public DataSource customDataSource() {
        return postgreSqlDataSource(appProps.postgresUrl(), appProps.postgresUser(),
                                    appProps.postgresPwd(), appProps.defaultSchema(), appProps.postgreSqlPoolSize());
    }

    @Bean(value = "jedisPool", destroyMethod = "close")
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(32);
        config.setMaxIdle(8);
        config.setMinIdle(4);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);

        return new JedisPool(config, appProps.redisHost(), appProps.redisPort(), 60);
    }

    @Bean(value = "javaEmailSender")
    public JavaMailSender javaEmailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(appProps.emailHost());

        mailSender.setUsername(appProps.emailUsername());
        mailSender.setPassword(appProps.emailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put(EMAIL_PROTOCOL, appProps.emailProtocol());
        props.put(EMAIL_SMTP_AUTH, appProps.emailAuth());
        props.put(EMAIL_SMTP_STARTTLS_ENABLE, appProps.emailStarttls());
        props.put(EMAIL_SMTP_STARTTLS_REQUIRED, appProps.emailStarttlsRequired());
        props.put(EMAIL_SMTP_TIMEOUT, appProps.emailTimeout());
        props.put(EMAIL_SMTP_CONNECTION_TIMEOUT, appProps.emailConnectionTimeout());
        return mailSender;
    }
}
