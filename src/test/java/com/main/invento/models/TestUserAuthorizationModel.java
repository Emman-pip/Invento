package com.main.invento.models;

import com.main.invento.binders.UserAuthorizationBinder;
import com.main.invento.utilityClasses.Encryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class TestUserAuthorizationModel {
    @Test
    public void testUserExists(){
        Assertions.assertFalse(UserAuthorizationModel.userExists("someone"));
    }
    @Test
    public void testSignin() throws NoSuchAlgorithmException {
        Assertions.assertNotNull(UserAuthorizationModel.signIn("emman2", Encryptor.SHA256("123123123")));
        Assertions.assertNull(UserAuthorizationModel.signIn("21390p847wejfhdnasl;fqwuieyrfois du0p9237ur wo;j0p39748 uw;fiu0p23874923uijsop7q230p47823", Encryptor.SHA256("123123123")));

    }
}
