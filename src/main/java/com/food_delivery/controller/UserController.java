package com.food_delivery.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food_delivery.conf.AppProps;
import com.food_delivery.constant.ErrorDictionary;
import com.food_delivery.exception.CommonException;
import com.food_delivery.exception.ErrorCode;
import com.food_delivery.model.AddressInfo;
import com.food_delivery.model.CustomerInfo;
import com.food_delivery.model.form.AddressForm;
import com.food_delivery.model.form.EmailForm;
import com.food_delivery.model.form.LoginForm;
import com.food_delivery.model.form.RegistrationForm;
import com.food_delivery.model.form.RegistrationPwdForm;
import com.food_delivery.model.form.ResetPasswordForm;
import com.food_delivery.model.form.VerifyRegisterOtpForm;
import com.food_delivery.model.view.AddressInfoView;
import com.food_delivery.model.view.LoginResponse;
import com.food_delivery.model.view.RegistrationInfoView;
import com.food_delivery.service.UserService;
import com.food_delivery.util.JWTUtils;

import lombok.extern.log4j.Log4j2;

@Controller
@RestController
@RequestMapping(value = "/user")
@CrossOrigin("*")
@Log4j2
@Validated
public class UserController {
    private final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password.";
    private final String PHONE_NUMBER                 = "phoneNumber";
    private final String MAIL                         = "mail";
    private final String CUSTOMER_ID                  = "customerId";
    private final String SECRET_KEY                   = "secretKey";
    private final String USER_NOT_FOUND               = "User not found";
    private final String FAIL_TO_SAVE_CUSTOMER_INFO   = "Failed to save customer information.";
    public final String INVALID_ADDRESS                     = "invalid address";

    @Autowired
    private AppProps appProps;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegistrationForm form) throws CommonException {
        Map<String, String> registerInfo = userService.register(form);

        String phone = registerInfo.get(PHONE_NUMBER);
        String mail = registerInfo.get(MAIL);
        String customerId = registerInfo.get(CUSTOMER_ID);

        RegistrationInfoView view = new RegistrationInfoView(phone, mail, customerId);
        return new ResponseEntity<>(view, HttpStatus.OK);
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<Object> verifyOtp(@RequestBody @Valid VerifyRegisterOtpForm form) throws CommonException {
        String secretKey = userService.verifyOtp(form);
        return new ResponseEntity<>(new HashMap<String, String>() {{put(SECRET_KEY, secretKey);}}, HttpStatus.OK);
    }

    @PostMapping("/register/save-info")
    public ResponseEntity<Object> saveInfo(@RequestBody @Valid RegistrationPwdForm form) throws CommonException {

        LoginResponse response = userService.saveRegistrationInfo(form);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginForm form) throws CommonException {
        String username = form.getUsername().toLowerCase().trim();
        String password = form.getPwd();
        String cert = form.getCertificate();

        Optional<CustomerInfo> customerInfo = userService.getCustomer(username, username);
        if (customerInfo.isPresent() && isMatchingPassword(password, customerInfo.get().getPassword())) {
            String token = JWTUtils.buildToken(username, customerInfo.get().getCustomerId(), appProps.tokenSigningKey());
            String refreshToken = JWTUtils.rebuildToken(token, appProps.tokenSigningKey(), cert);
            return new ResponseEntity<>(new LoginResponse(token, refreshToken), HttpStatus.OK);
        } else {
            throw new CommonException(INVALID_USERNAME_OR_PASSWORD,
                                      ErrorDictionary.INVALID_USERNAME_OR_PASSWORD,
                                      ErrorCode.AUTHENTICATION);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody @Valid EmailForm form) throws CommonException {
        String email = form.getRecipient();
        Optional<CustomerInfo> customerInfoOpt = userService.getCustomer(email, email);
        if (customerInfoOpt.isPresent()) {
            String secretKey = userService.saveCacheAndSendMail(customerInfoOpt.get(), form);
            return new ResponseEntity<>(new HashMap<String, String>() {{put(SECRET_KEY, secretKey);}}, HttpStatus.OK);
        } else {
            throw new CommonException(USER_NOT_FOUND,
                                      ErrorDictionary.USER_NOT_FOUND,
                                      ErrorCode.ITEM_NOT_FOUND);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody @Valid ResetPasswordForm form) throws CommonException {
        int resetResult = userService.resetPassword(form);
        if (resetResult > 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new CommonException(FAIL_TO_SAVE_CUSTOMER_INFO, ErrorDictionary.FAIL_TO_SAVE_CUSTOMER_INFO, ErrorCode.BAD_REQUEST_PARAMS);
        }
    }

    @PostMapping("/address")
    public ResponseEntity<Object> saveAddress(@RequestBody @Valid AddressForm form) throws CommonException {
        int result = userService.addCustomerAddress(form);
        if (result > 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new CommonException(FAIL_TO_SAVE_CUSTOMER_INFO, ErrorDictionary.FAIL_TO_SAVE_CUSTOMER_INFO, ErrorCode.BAD_REQUEST_PARAMS);
        }
    }

    @GetMapping("/addresses")
    public ResponseEntity<AddressInfoView> getAddresses() {
        List<AddressInfo> addressInfoList = userService.getAddressesByCustomer();
        AddressInfoView view = new AddressInfoView(addressInfoList.size(), addressInfoList);
        return new ResponseEntity<>(view, HttpStatus.OK);
    }

    private boolean isMatchingPassword(String password, String hashPass) throws CommonException {
        try {
            return passwordEncoder.matches(password, hashPass);
        } catch (Exception e) {
            throw new CommonException(INVALID_USERNAME_OR_PASSWORD,
                                      ErrorDictionary.INVALID_USERNAME_OR_PASSWORD,
                                      ErrorCode.AUTHENTICATION);
        }
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
