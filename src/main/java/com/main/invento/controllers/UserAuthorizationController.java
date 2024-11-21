package com.main.invento.controllers;

import com.main.invento.binders.UserAuthorizationBinder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    public void signUp(){
        boolean isValid = UserAuthorizationBinder.validateUser(username) && UserAuthorizationBinder.validateFields(email) && UserAuthorizationBinder.validateFields(password, confirmPassword);
        if (isValid){
            warning.setText("Welcome!!!");
            warning.setStyle("-fx-text-fill: green");
        } else {
            warning.setText("Please recheck all the fields.");
            warning.setStyle("-fx-text-fill: red");
        }
    }
}
