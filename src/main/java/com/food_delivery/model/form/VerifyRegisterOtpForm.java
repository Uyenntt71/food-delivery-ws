package com.food_delivery.model.form;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyRegisterOtpForm {
    @NotEmpty
    private String otp;

    @NotEmpty
    private String customerId;

}
