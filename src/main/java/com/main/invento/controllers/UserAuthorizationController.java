package com.main.invento.controllers;

import com.main.invento.Main;
import com.main.invento.binders.UserAuthorizationBinder;
import com.main.invento.models.UserAuthorizationModel;
import com.main.invento.utilityClasses.Database;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.io.IOException;
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
    private void login() throws NoSuchAlgorithmException, IOException {
        ObjectId id = UserAuthorizationBinder.bindLogin(this.loginUsername, this.loginPassword);
        if (id == null) {
            loginWarning.setText("Invalid credentials");
            loginWarning.setStyle("-fx-text-fill: red");
        } else {
            loginWarning.setText("welcome!");
            loginWarning.setStyle("-fx-text-fill: green");


            // for logging
            UserAuthorizationModel.logLogin(this.loginUsername.getText());

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/user-dashboard-view.fxml") );

            Parent loaded = loader.load();
            UserDashboardController dashboard = (UserDashboardController) loader.getController();
            dashboard.setUsername(this.loginUsername.getText());

            Stage stage = new Stage();
            Scene scene = new Scene(loaded);
            scene.getStylesheets().add(Main.class.getResource("styles/dashboardStyles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();

            Stage toClose = (Stage) this.loginPassword.getScene().getWindow();
            toClose.close();
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
        scene.getStylesheets().add(Main.class.getResource("styles/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.show();
    }


    public static void buttonHoverAnimation(Button btn, String color1, String color2, String defaultColor){
        btn.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btn.setStyle(
                        "-fx-background-color: " + color1 + ";" +
                                "-fx-background-radius:10em;" +
                                "-fx-border-color: " + color1 + ";" +
                                "-fx-border-radius: 10em;" +
                                "-fx-text-fill: " + color2
                );
            }
        });
        btn.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btn.setStyle(
                        "-fx-background-color: " + defaultColor + ";" +
                                "-fx-background-radius:10em;" +
                                "-fx-border-color: " + color1 + ";" +
                                "-fx-border-radius: 10em;" +
                                "-fx-text-fill: " + color1);
            }
        });
    }

    @FXML
    private Button whiteBtn;
    @FXML
    private Button greenBtn;

    public void initialize(){
        buttonHoverAnimation(whiteBtn, "#E8F3F1", "#5a9d8d", "transparent");
        buttonHoverAnimation(greenBtn, "#5a9d8d", "#E8F3F1", "transparent");

    }

}
