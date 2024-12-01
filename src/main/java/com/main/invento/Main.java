package com.main.invento;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws  Exception{
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/user-authorization-view.fxml"));
        Scene scene =  new Scene(loader.load());
        scene.getStylesheets().add(Main.class.getResource("styles/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Signup");
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args){
        launch();
    }
}
