package com.food_delivery.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.food_delivery.exception.CommonException;
import com.food_delivery.model.AddressInfo;
import com.food_delivery.model.CustomerInfo;
import com.food_delivery.model.form.AddressForm;
import com.food_delivery.model.form.EmailForm;
import com.food_delivery.model.form.RegistrationForm;
import com.food_delivery.model.form.RegistrationPwdForm;
import com.food_delivery.model.form.ResetPasswordForm;
import com.food_delivery.model.form.VerifyRegisterOtpForm;
import com.food_delivery.model.view.LoginResponse;

@Service
public interface UserService {
    Map<String, String> register(RegistrationForm form) throws CommonException;

    String verifyOtp(VerifyRegisterOtpForm form) throws CommonException;

    LoginResponse saveRegistrationInfo(RegistrationPwdForm form) throws CommonException;

    String saveCacheAndSendMail(CustomerInfo customerInfo, EmailForm emailForm) throws CommonException;

    int resetPassword(ResetPasswordForm form);

    Optional<CustomerInfo> getCustomer(String email, String phone);

   int addCustomerAddress(AddressForm form) throws CommonException;

   List<AddressInfo> getAddressesByCustomer();
}
