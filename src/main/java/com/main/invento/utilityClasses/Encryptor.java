package com.main.invento.utilityClasses;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {
    public static String SHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        byte[] hashed = digest.digest();
        return String.format("%64x", new BigInteger(1, hashed));
    }
}
