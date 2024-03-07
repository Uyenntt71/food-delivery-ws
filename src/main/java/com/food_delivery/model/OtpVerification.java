package com.food_delivery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OtpVerification {
    private String otp;
    private int tryTime;

    public OtpVerification(String otp) {
        this.otp = otp;
        this.tryTime = 0;
    }
}
