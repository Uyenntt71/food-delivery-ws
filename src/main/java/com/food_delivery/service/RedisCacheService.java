package com.food_delivery.service;

import com.food_delivery.model.OtpVerification;

public interface RedisCacheService {
    public void set(String key, String value, int expire);

    public void del(String key);

    public void set(String key, String value);

    public String get(String key);

    void setOtp(String key, OtpVerification otpVerification);

    void updateOtp(String key, OtpVerification otpVerification);

    OtpVerification getOtp(String key);
}
