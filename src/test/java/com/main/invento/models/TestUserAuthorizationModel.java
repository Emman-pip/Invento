package com.main.invento.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestUserAuthorizationModel {
    @Test
    public void testUserExists(){
        Assertions.assertFalse(UserAuthorizationModel.userExists("someone"));
    }
}
