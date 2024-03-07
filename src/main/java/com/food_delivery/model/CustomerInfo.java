package com.food_delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInfo {
    private String customerId;
    private String email;
    private String phoneNumber;
    private String password;
    private String name;

    @JsonIgnore
    private String address;

    @JsonIgnore
    private String photo;

}
