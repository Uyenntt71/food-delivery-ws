package com.food_delivery.model.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationForm {
    @NotEmpty
    @Pattern(regexp = "(84[3|5|7|8|9]|0[3|5|7|8|9])+([0-9]{8})\\b", message = "Invalid phone number format")
    private final String phoneNumber;

    @NotEmpty
    private final String name;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private final String email;

}
