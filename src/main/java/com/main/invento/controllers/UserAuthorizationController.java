package com.main.invento.controllers;

import com.main.invento.binders.UserAuthorizationBinder;
import com.main.invento.models.UserAuthorizationModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.security.NoSuchAlgorithmException;

public class UserAuthorizationController {
    // make a controller here
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private TextField organization;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label warning;

    @FXML
    public void signUp() throws NoSuchAlgorithmException {
        boolean isValid = UserAuthorizationBinder.validateUser(username) && UserAuthorizationBinder.validateFields(email) && UserAuthorizationBinder.validateFields(password, confirmPassword);
        if (isValid){
            warning.setText("Welcome!!!");
            warning.setStyle("-fx-text-fill: green");
            new UserAuthorizationModel().addUser(this.username.getText(), this.email.getText(), this.organization.getText(), this.password.getText());
        } else {
            warning.setText("Please recheck all the fields.");
            warning.setStyle("-fx-text-fill: red");
        }
    }
}
