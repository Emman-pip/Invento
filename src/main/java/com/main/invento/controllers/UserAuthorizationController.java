package com.main.invento.controllers;

import com.main.invento.Main;
import com.main.invento.binders.UserAuthorizationBinder;
import com.main.invento.models.UserAuthorizationModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

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
            UserAuthorizationBinder.bindSignup(this.username.getText(), this.email.getText(), this.organization.getText(), this.password.getText());
            warning.setStyle("-fx-text-fill: green");
        } else {
            warning.setText("Please recheck all the fields.");
            warning.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    private TextField loginUsername;
    @FXML
    private PasswordField loginPassword;

    @FXML
    private void toSignup() throws Exception{
        Stage toClose = (Stage) this.loginUsername.getScene().getWindow();
        toClose.close();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/user-authorization-view.fxml"));
        Scene scene =  new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Signup");
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private Label loginWarning;

    @FXML
    private void login() throws NoSuchAlgorithmException {
        ObjectId id = UserAuthorizationBinder.bindLogin(this.loginUsername, this.loginPassword);
        if (id == null) {
            loginWarning.setText("Invalid credentials");
            loginWarning.setStyle("-fx-text-fill: red");
        } else {
            loginWarning.setText("welcome!");
            loginWarning.setStyle("-fx-text-fill: green");
            UserAuthorizationModel.logLogin(this.loginUsername.getText());
            // pass id to a new window, the dashboard screen
        }
    }

    @FXML
    private void toLogin() throws Exception {
        Stage toClose = (Stage) this.confirmPassword.getScene().getWindow();
        toClose.close();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/login-view.fxml"));
        Scene scene =  new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.show();
    }
}
