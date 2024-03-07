package com.food_delivery.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.food_delivery.conf.AppProps;

@Component
public class ClientCryptImpl implements ClientCrypt {
    private final String        CIPHER_INSTANCE = "AES/ECB/PKCS5Padding";
    private       SecretKeySpec secretKeySpec;
    private       byte[]        key;

    public ClientCryptImpl(AppProps appProps) {
        key = Arrays.copyOf(appProps.salt().getBytes(StandardCharsets.UTF_8), 16);
        secretKeySpec = new SecretKeySpec(key, "AES");
    }

    @Override
    public String encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }

    @Override
    public String decrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
