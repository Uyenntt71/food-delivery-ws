package com.food_delivery.service;

import com.food_delivery.exception.CommonException;
import com.food_delivery.model.form.EmailForm;

public interface EmailService {
    void sendEmail(EmailForm form) throws CommonException;

    String sendMailWithAttachment(EmailForm form);

}
