package com.food_delivery.service;

import static com.food_delivery.util.CommonUtils.generateRandomUuidAsString;
import static com.food_delivery.util.CommonUtils.getCustomerId;
import static com.food_delivery.util.CommonUtils.normalizePhoneNumber;
import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.food_delivery.conf.AppProps;
import com.food_delivery.constant.ErrorDictionary;
import com.food_delivery.dao.UserDao;
import com.food_delivery.exception.CommonException;
import com.food_delivery.exception.ErrorCode;
import com.food_delivery.model.AddressInfo;
import com.food_delivery.model.CustomerInfo;
import com.food_delivery.model.OtpVerification;
import com.food_delivery.model.form.AddressForm;
import com.food_delivery.model.form.EmailForm;
import com.food_delivery.model.form.RegistrationForm;
import com.food_delivery.model.form.RegistrationPwdForm;
import com.food_delivery.model.form.ResetPasswordForm;
import com.food_delivery.model.form.VerifyRegisterOtpForm;
import com.food_delivery.model.view.LoginResponse;
import com.food_delivery.util.CacheUtils;
import com.food_delivery.util.JWTUtils;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserServiceImpl implements UserService{
    private final String EMAIL_OR_PHONE_NUMBER_ALREADY_EXIST = "Email or phone number already exist.";
    private final String PHONE_NUMBER                        = "phoneNumber";
    private final String MAIL                                = "mail";
    private final String NAME                                = "name";
    private final String CUSTOMER_ID                         = "customerId";
    private final String SECRET_KEY                          = "secretKey";
    private final String INVALID_OTP                         = "Invalid OTP.";
    public static final String MAX_NUMBER_OF_LOCATION_5 = "Max number of location: 5";
    private final String FAIL_TO_SAVE_CUSTOMER_INFO   = "Failed to save customer information.";

    @Autowired
    private UserDao userDao;

    @Autowired
    private ClientCrypt crypt;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private AppProps appProps;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Override
    public Map<String, String> register(RegistrationForm form) throws CommonException {
        String name = form.getName();
        String email = form.getEmail();
        String phoneNumber = normalizePhoneNumber(form.getPhoneNumber());

//        Optional<CustomerInfo> customerOpt = this.getCustomer(email, phoneNumber);
//        if (customerOpt.isPresent()) {
//            throw new CommonException(EMAIL_OR_PHONE_NUMBER_ALREADY_EXIST,
//                                      ErrorDictionary.EMAIL_OR_PHONE_NUMBER_ALREADY_EXIST,
//                                      ErrorCode.INVALID_ARGUMENTS);
//        }

        String customerId = generateRandomUuidAsString();

        CustomerInfo ci = new CustomerInfo(customerId, email, phoneNumber, null, name, null, null);
        String ciCache = CacheUtils.objToString(ci);
        String secretKey = crypt.encrypt(customerId);
        redisCacheService.set(secretKey, ciCache);
        redisCacheService.setOtp(customerId, new OtpVerification(appProps.realOtp()));

        HashMap<String, String> registerInfo = new HashMap<>();
        registerInfo.put(CUSTOMER_ID, customerId);
        registerInfo.put(PHONE_NUMBER, phoneNumber);
        registerInfo.put(MAIL, email);

        return registerInfo;
    }

    @Override
    public String verifyOtp(VerifyRegisterOtpForm form) throws CommonException {
        String otp = form.getOtp();
        String customerId = form.getCustomerId();

        OtpVerification realOtp = redisCacheService.getOtp(customerId);

        if (!isValidOtp(realOtp, otp)) {
            invalidOtp(realOtp);
        } else {
            String secretKey = crypt.encrypt(customerId);
            return secretKey;
        }
        return null;
    }

    private boolean isValidOtp(OtpVerification realOtp, String otp) {
        return !isNull(realOtp) && realOtp.getOtp().equals(otp) && realOtp.getTryTime() < appProps.otpTryTime();
    }

    private void invalidOtp(OtpVerification realOtp) throws CommonException {
        if (!isNull(realOtp)) {
            realOtp.setTryTime(realOtp.getTryTime() + 1);
        }

        throw new CommonException(INVALID_OTP, ErrorDictionary.INVALID_OTP, ErrorCode.INVALID_ARGUMENTS);
    }

    @Override
    public LoginResponse saveRegistrationInfo(RegistrationPwdForm form) throws CommonException {
        CustomerInfo customerInfo = getCustomerInfo(form);

        Optional<CustomerInfo> customerInfoOpt = userDao.saveCustomer(customerInfo);
        if (customerInfoOpt.isPresent()) {
            String username = customerInfo.getName();
            String customerId = customerInfo.getCustomerId();
            String token = JWTUtils.buildToken(username, customerId, appProps.tokenSigningKey());
            String refreshToken = JWTUtils.rebuildToken(token, appProps.tokenSigningKey(), form.getCertificate());
            return new LoginResponse(token, refreshToken);
        } else {
            throw new CommonException(FAIL_TO_SAVE_CUSTOMER_INFO, ErrorDictionary.FAIL_TO_SAVE_CUSTOMER_INFO, ErrorCode.BAD_REQUEST_PARAMS);
        }
    }

    @Override
    public String saveCacheAndSendMail(CustomerInfo customerInfo, EmailForm emailForm) throws CommonException {
        String customerId = customerInfo.getCustomerId();
        String secretKey = crypt.encrypt(customerId);

        emailService.sendEmail(emailForm);

        return secretKey;
    }

    private CustomerInfo getCustomerInfo(RegistrationPwdForm form) throws CommonException {
        String secretKey = form.getSecretKey();
        String customerInfoString = redisCacheService.get(secretKey);

        if (customerInfoString == null || customerInfoString.isEmpty()) {
            throw new CommonException(FAIL_TO_SAVE_CUSTOMER_INFO, ErrorDictionary.FAIL_TO_SAVE_CUSTOMER_INFO, ErrorCode.BAD_REQUEST_PARAMS);

        }

        CustomerInfo ci = (CustomerInfo) CacheUtils.stringToObject(customerInfoString, CustomerInfo.class);
        String password = passwordEncoder.encode(form.getPwd());
        ci.setPassword(password);
        return ci;
    }

    @Override
    public int resetPassword(ResetPasswordForm form) {
        String secretKey = form.getSecretKey();
        String customerId = crypt.decrypt(secretKey);

        String password = passwordEncoder.encode(form.getPassword());
        return userDao.updatePassword(customerId, password);
    }

    @Override
    public Optional<CustomerInfo> getCustomer(String email, String phone) {
        return userDao.getCustomer(email, phone);
    }


    @Override
    public int addCustomerAddress(AddressForm form) throws CommonException {
        String customerId = getCustomerId();

        int totalAddress = userDao.countCustomerAddress(customerId);
        if (totalAddress >= appProps.maxAddress()) {
            throw new CommonException(MAX_NUMBER_OF_LOCATION_5,
                                      ErrorDictionary.LOCATION_OVER_LIMIT,
                                      ErrorCode.PERMISSION_DENIED);
        }

        return userDao.addCustomerAddress(customerId, form.getName(), form.getAddress(), form.getLat(), form.getLng());
    }

    @Override
    public List<AddressInfo> getAddressesByCustomer() {
        String customerId = getCustomerId();

        return userDao.getAddressesByCustomerId(customerId);
    }

}
