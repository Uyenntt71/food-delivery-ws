package com.food_delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSession {
    private String id;
    private String customerId;
    private String certificateKey;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private String refreshToken;
    private String brand;
    private String model;
    private Boolean active;
    private String os;
    private String osVersion;
    private String deviceName;
    private long lastActive;
}
