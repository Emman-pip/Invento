package com.main.invento.controllers;

import com.main.invento.models.InventoryModel;
import com.main.invento.utilityClasses.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class CreateInventoryController {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    private TextField inventoryName;

    private void addToOwnedInventories(ObjectId inventoryId){
        MongoCollection<Document> db = new Database().getConnection("Users");
        Bson filter = Filters.eq("username", this.username);
        Bson update = Updates.push("ownedInventories", inventoryId);
        db.findOneAndUpdate(filter, update);
    }

    @FXML
    public void createNewInventory(){
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        InventoryModel newInventory = new InventoryModel(inventoryName.getText());

        // update the owned inventories of the users
        addToOwnedInventories(newInventory.getInventoryId());

        newInventory.initializeModel();
        db.insertOne(newInventory.getInventory());
        Stage current = (Stage)inventoryName.getScene().getWindow();
        current.close();
        this.parent.loadOwnedInventories();
    }

    private UserDashboardController parent;
    public void setParent(UserDashboardController root){
        this.parent = root;
    }
}
