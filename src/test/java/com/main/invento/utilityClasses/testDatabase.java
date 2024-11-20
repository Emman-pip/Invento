package com.main.invento.utilityClasses;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testDatabase {
    @Test
    public void testVerifyConnection(){
        Assertions.assertNotNull(new Database().getConnection("inventoDummy"));
    }
}
