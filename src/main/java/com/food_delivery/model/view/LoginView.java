package com.food_delivery.model.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginView {
    private String token;
    private String refreshToken;
    private String vmqToken;
    private String customerId;
    private String accountId;
    private String email;
    private String phoneNumber;
    private String name;
    private String language;
    private String voiceId;
    private String prefix;
}
