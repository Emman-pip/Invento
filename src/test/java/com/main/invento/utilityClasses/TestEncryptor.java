package com.main.invento.utilityClasses;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Set;

public class TestEncryptor {
    @Test
    public void testEncryptor() {
        HashMap<String, String> data =  new HashMap<String, String>();
        data.put("haha", "90b235e9eb8f197f2dd927937222c570396d971222d9009a9189e2b6cc0a2c1");
        data.put("hello", "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
        data.put("hehe","ebe2eca800cf7bd9d9d9f9f4aafbc0c77ae155f43bbbeca69cb256a24c7f9bb");
        Set<String> keys = data.keySet();
        keys.forEach(key -> {
            try {
                Assertions.assertEquals(data.get(key), Encryptor.SHA256(key).strip());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
