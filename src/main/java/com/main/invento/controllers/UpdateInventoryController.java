package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class UpdateInventoryController {
    private String username;
    private ObjectId inventoryId;
    private UserDashboardController parent;

    public void setParent(UserDashboardController parent) {
        this.parent = parent;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInventoryId(ObjectId inventoryId) {
        this.inventoryId = inventoryId;
    }

    @FXML
    private TextField newName;

    @FXML
    private  void updateName(){
        Bson filter = Filters.eq("_id", this.inventoryId);
        Bson update = Updates.set("inventoryName", newName.getText());
        MongoCollection<Document> db = new Database().getConnection("Inventories");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("This will change the name of the inventory");
        alert.showAndWait();

        if (alert.getResult() != ButtonType.OK){
            return;
        }
        db.findOneAndUpdate(filter,update);
        UserDashboardController parentScreen = parent;
        parentScreen.loadOwnedInventories();

        Stage stage = (Stage)newName.getScene().getWindow();
        stage.close();

        Alert done = new Alert(Alert.AlertType.INFORMATION);
        done.setContentText("Updated the name of the inventory.");

        done.showAndWait();

    }
}
