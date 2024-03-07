package com.food_delivery.model.form;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginForm {
    @NotEmpty
    private final String username;

    @NotEmpty
    private final String pwd;

    private final String certificate;

    private final String brand;

    private final String model;

    private final String appVersion;

    private final String os;

    private final String deviceName;

    private final String osVersion;
}
