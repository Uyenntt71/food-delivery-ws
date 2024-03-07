package com.food_delivery.service;

import static org.apache.logging.log4j.util.Strings.isNotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.food_delivery.conf.AppProps;
import com.food_delivery.model.OtpVerification;
import com.google.gson.Gson;

import lombok.extern.log4j.Log4j2;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
@Log4j2
public class RedisCacheServiceImpl implements RedisCacheService {
    public static final  int  DEFAULT_EXPIRATION = 300;
    private static final Gson gson               = new Gson();

    @Autowired
    AppProps appProps;

    @Autowired
    @Qualifier("jedisPool")
    private JedisPool jedisPool;

    @Override
    public void set(String key, String value, int expire) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
            jedis.expire(key, expire);
        } catch (Exception e) {
            log.warn("Exception when save cache to Redis.", e);
        }
    }

    @Override
    public void del(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(key);
        } catch (Exception e) {
            log.warn("Failed to remove relation with key: " + key);
        }
    }

    public void set(String key, String value) {
        set(key, value, DEFAULT_EXPIRATION);
    }

    @Override
    public String get(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.get(key);
        } catch (Exception e) {
            log.warn("Failed to get relation with key: " + key);
        }
        return null;
    }

    @Override
    public void setOtp(String key, OtpVerification otpVerification) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, otpVerificationToString(otpVerification));
            jedis.expire(key, DEFAULT_EXPIRATION);
        } catch (Exception e) {
            log.warn("Exception when save cache to Redis.", e);
        }
    }

    private String otpVerificationToString(OtpVerification otpVerification) {
        return gson.toJson(otpVerification);
    }

    @Override
    public void updateOtp(String key, OtpVerification otpVerification) {
        try (Jedis jedis = jedisPool.getResource()) {
            long ttl = jedis.ttl(key);
            jedis.set(key, otpVerificationToString(otpVerification));
            jedis.expire(key, (int) ttl);
        } catch (Exception e) {
            log.warn("Exception found when update cache from Redis.", e);
        }
    }

    @Override
    public OtpVerification getOtp(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            if (isNotEmpty(value)) return stringToOtpVerification(value);
        } catch (Exception e) {
            log.warn("Exception found when get cache from Redis.", e);
        }

        return null;
    }

    private static OtpVerification stringToOtpVerification(String otp) {
        return gson.fromJson(otp, OtpVerification.class);
    }
}
