package com.main.invento.controllers;

import com.main.invento.Main;
import com.main.invento.utilityClasses.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDashboardController {
    private String username;
    private Document userData;

    @FXML
    private Label welcome;

    public void setUsername(String username) {
        this.username = username;
        welcome.setText("Welcome, " + this.username);
        loadUserData();
    }

    private void loadUserData(){
        // retrieve and assign the user data to the userData attribute
        this.userData = new Database().getConnection("Users").find(new Document("username", username)).first();
//      analytics.getChildren().add(new Label(Arrays.toString(new ArrayList[]{(ArrayList<String>) this.userData.get("ownedInventories")})));
//      analytics.getChildren().add(new Label("" + userData.get("_id")));
      // function to get the keys of a document
//      System.out.println(userData.keySet());
    }

    @FXML
    private HBox analytics;

    @FXML
    private ImageView inventoryIcon;
    @FXML
    public void initialize(){
    }

    @FXML
    private void createNewInventory() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/create-inventory.fxml"));
        Parent root = loader.load();
        CreateInventoryController controller = (CreateInventoryController) loader.getController();
        controller.setUsername((String) this.userData.get("username"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
