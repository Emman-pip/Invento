package com.main.invento.binders;

import com.main.invento.models.UserAuthorizationModel;
import com.main.invento.utilityClasses.Encryptor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.types.ObjectId;

import java.security.NoSuchAlgorithmException;

public class UserAuthorizationBinder {
    public static boolean validateFields(TextField email) {
        boolean flag = email.getText().contains("@");
        if (!flag) {
            email.styleProperty().set("-fx-border-color: red; -fx-background-color: #D9D9D9");
        } else email.styleProperty().set("-fx-border-color: none; -fx-background-color: #D9D9D9");
        return flag;
    }

    public static boolean validateFields(PasswordField password, PasswordField confirmPassword) {
        boolean flag = password.getText().equals(confirmPassword.getText()) && password.getText().length() > 8;
        if (!flag) {
            password.setStyle("-fx-border-color: red; -fx-background-color: #D9D9D9");
            confirmPassword.styleProperty().set("-fx-border-color: red; -fx-background-color: #D9D9D9");
        } else {
            password.setStyle("-fx-border-color: none; -fx-background-color: #D9D9D9");
            confirmPassword.styleProperty().set("-fx-border-color: none; -fx-background-color: #D9D9D9");
        }
        return flag;
    }

    public static boolean validateUser(TextField username) {
        boolean flag = UserAuthorizationModel.userExists(username.getText());
        if (flag || username.getText().strip().length() < 4) {
            username.setStyle("-fx-border-color: red; -fx-background-color: #D9D9D9");
        } else {
            username.styleProperty().set("-fx-border-color: none; -fx-background-color: #D9D9D9");
        }
        return !flag && username.getText().strip().length() >= 4;
    }

    public static void bindSignup(String username, String email, String organization, String password) throws NoSuchAlgorithmException {
        new UserAuthorizationModel().addUser(username, email, organization, password);
    }


    public static ObjectId bindLogin(TextField username, PasswordField password) throws NoSuchAlgorithmException {
        return UserAuthorizationModel.signIn(username.getText(), Encryptor.SHA256(password.getText()));
    }
}
