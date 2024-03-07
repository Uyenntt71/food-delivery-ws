package com.food_delivery.util;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.security.core.context.SecurityContextHolder;

import com.food_delivery.exception.CommonException;
import com.food_delivery.exception.ErrorCode;

public class CommonUtils {
    private static final String PHONE_NUMBER_PREFIX         = "0";
    private static final String COUNTRY_PHONE_NUMBER_PREFIX = "84";
    private static final String INVALID_LATITUDE = "Latitude must be between -90 and 90.";
    private static final String INVALID_LONGITUDE = "Longitude must be between -180 and 180.";

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public static String generateRandomUuidAsString() {
        return UUID.randomUUID().toString();
    }

    public static boolean isPhoneNumber(String text) {
        String pattern = "(84[3|5|7|8|9]|0[3|5|7|8|9])+([0-9]{8})\\b";
        Pattern p = Pattern.compile(pattern);

        return p.matcher(text).matches();
    }

    public static String normalizePhoneNumber(String number) {
        if (number.startsWith(COUNTRY_PHONE_NUMBER_PREFIX)) {return PHONE_NUMBER_PREFIX.concat(number.substring(2));}
        return number;
    }

    public static String getCustomerId() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

    public static void validateLocation(double lat, double lng) throws CommonException {
        if (lat > 90 || lat < -90) {
            throw new CommonException(INVALID_LATITUDE, ErrorCode.INVALID_ARGUMENTS);
        }
        if (lng > 180 || lng < -180) {
            throw new CommonException(INVALID_LONGITUDE, ErrorCode.INVALID_ARGUMENTS);
        }
    }
}
