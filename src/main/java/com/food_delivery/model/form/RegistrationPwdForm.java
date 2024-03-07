package com.food_delivery.model.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class RegistrationPwdForm {

    @NotEmpty
    private final String secretKey;

    @NotEmpty
    @Pattern(regexp = "(?=^.{8,16}$)(?=.*\\d)(?=.*[!@#$%^&*_])(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
             message = "Password must be 8-16 characters, contain at least one special character," +
                       "at least one uppercase alphabetical character, at least one number")
    private final String pwd;

    private final String certificate;

    private final String brand;

    private final String model;

    private final String os;

    private final String deviceName;

    private final String osVersion;

    private final String appVersion;
}
