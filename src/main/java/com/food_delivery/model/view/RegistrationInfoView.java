package com.food_delivery.model.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationInfoView {
    private String phoneNumber;
    private String email;
    private String customerId;
//    private String secretKey;
}
