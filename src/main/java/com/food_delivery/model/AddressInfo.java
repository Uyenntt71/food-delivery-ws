package com.food_delivery.model;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddressInfo {
    private String id;

    private String name;

    private String address;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
