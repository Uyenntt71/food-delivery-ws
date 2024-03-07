package com.food_delivery.model.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddressForm {
    private String name;

    private String address;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
