package com.food_delivery.model;

import lombok.Data;

@Data
public class Restaurant {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private Double lat;
    private Double lng;
    private Double distanceToCustomer;
}
