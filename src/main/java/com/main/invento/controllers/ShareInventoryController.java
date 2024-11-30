package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.InventoryLogger;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ShareInventoryController {
    private ObjectId inventoryId;
    private Document inventoryData;


    public void setInventoryId(ObjectId inventoryId) {
        this.inventoryId = inventoryId;
        reloadInventoryData();
        this.inventoryName.setText("Add users you " + inventoryData.get("inventoryName"));
        loadUsers();
    }

    private void reloadInventoryData(){
        inventoryData = new Database().getConnection("Inventories").find(new Document("_id", inventoryId)).first();
    }

    @FXML
    private Label inventoryName;

    @FXML
    private TextField newUser;

    @FXML
    private VBox collaborations;

    private void loadUsers(){
        collaborations.getChildren().clear();
        Iterable<String> users =  (Iterable<String>)this.inventoryData.get("sharedTo");
        int count = 0 ;
        for (String user : users) {
            BorderPane bp = new BorderPane();
            bp.setRight(new Label(user));
            Button btn= new Button("delete");

            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    pullUsersFromShared(user);
                }
            });
            bp.setLeft(btn);
            collaborations.getChildren().add(bp);
            count++;
        }
        if (count == 0){
            collaborations.getChildren().add(new Label("No collaborators yet..."));
        }

    }

    @FXML
    private void addUser(){
        String userToAdd = this.newUser.getText();
        pushUsersToShared(userToAdd);
        loadUsers();
    }


    private void pullUsersFromShared(String username){
        MongoCollection<Document> inventoryDB = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id",inventoryId);
        Bson update = Updates.pull("sharedTo", username);
        System.out.println(username);
        inventoryDB.findOneAndUpdate(filter, update);
        reloadInventoryData();
        loadUsers();
    }

    private void pushUsersToShared(String username){
//        MongoCollection<Document> db = new Database().getConnection("Inventories");
//        Bson filter = Filters.eq("_id", inventoryData.get("_id"));
//        Bson update;
//        if (willPush){
//            update = Updates.push("sharedTo", username);
//        } else {
//            update = Updates.pull("sharedTo", username);
//        }
//        db.findOneAndUpdate(filter, update);
        /////
        Document user = new Database().getConnection("Users").find(new Document("username", username)).first();
        MongoCollection<Document> inventoryDB = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id",inventoryId);
        Bson update = Updates.push("sharedTo", username);

        Iterable<String> sharedTo= (Iterable<String>) inventoryDB.find(filter).first().get("sharedTo");
        for (String userInside: sharedTo){
            if (userInside.equals( username)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("The user you are adding already have access");
                alert.showAndWait();
                return;
            }
        }

        if (user == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("User does not exist");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("This user will be able to access the data inside the inventory after this action.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK){
            inventoryDB.findOneAndUpdate(filter, update);
        }
        reloadInventoryData();
    }


}
