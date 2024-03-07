package com.food_delivery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.food_delivery.conf.AppProps;
import com.food_delivery.constant.ErrorDictionary;
import com.food_delivery.exception.CommonException;
import com.food_delivery.exception.ErrorCode;
import com.food_delivery.model.form.EmailForm;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class EmailServiceImpl implements EmailService {
    private static final String CAN_NOT_SEND_EMAIL = "Can't send email";
    @Autowired
    AppProps appProps;
    @Autowired
    @Qualifier(value = "javaEmailSender")
    private JavaMailSender javaMailSender;

    public void sendEmail(EmailForm form) throws CommonException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(appProps.emailUsername());
            message.setTo(form.getRecipient());
            message.setText(appProps.emailBody());
            message.setSubject(appProps.emailSubject());

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new CommonException(CAN_NOT_SEND_EMAIL, ErrorDictionary.CAN_NOT_SEND_EMAIL, ErrorCode.INVALID_ARGUMENTS);
        }
    }

    public String sendMailWithAttachment(EmailForm form) {
        return null;
    }

}
