package com.main.invento.binders;

import com.main.invento.models.UserAuthorizationModel;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserAuthorizationBinder {
    public static boolean validateFields(TextField email){
        boolean flag = email.getText().contains("@");
        if (!flag){
            email.styleProperty().set("-fx-border-color: red; -fx-background-color: #D9D9D9");
        } else email.styleProperty().set("-fx-border-color: none; -fx-background-color: #D9D9D9");
        return flag;
    }
    public static boolean validateFields(PasswordField password, PasswordField confirmPassword){
        boolean flag = password.getText().equals(confirmPassword.getText()) && password.getText().length() > 8;
        if (!flag){
            password.setStyle("-fx-border-color: red; -fx-background-color: #D9D9D9");
            confirmPassword.styleProperty().set("-fx-border-color: red; -fx-background-color: #D9D9D9");
        } else {
            password.setStyle("-fx-border-color: none; -fx-background-color: #D9D9D9");
            confirmPassword.styleProperty().set("-fx-border-color: none; -fx-background-color: #D9D9D9");
        }
        return flag;
    }

    public static boolean validateUser(TextField username){
        boolean flag = UserAuthorizationModel.userExists(username.getText());
        if (flag || username.getText().strip().length() < 4){
            username.setStyle("-fx-border-color: red; -fx-background-color: #D9D9D9");
        } else {
            username.styleProperty().set("-fx-border-color: none; -fx-background-color: #D9D9D9");
        }
        return !flag && username.getText().strip().length() >= 4;
    }
}
