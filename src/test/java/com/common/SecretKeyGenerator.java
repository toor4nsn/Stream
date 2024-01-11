package com.common;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretKeyGenerator {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

        SecureRandom secureRandom = new SecureRandom();
        int keyBitSize = 128;
        keyGenerator.init(keyBitSize, secureRandom);

        SecretKey secretKey = keyGenerator.generateKey();
        String s = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(s.length());

        System.out.println(s);
    }

}