package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;

public class ShareInventoryController {
    private Document userData;

    public void setUserData(Document userData) {
        this.userData = userData;
        listInventories();
    }

    @FXML
    private VBox toShareContainer;

    @FXML
    private ScrollPane scrollpane;

    private void inititalize(){
        scrollpane.setFitToWidth(true);
    }
    @FXML
    private VBox usersWithAccessContainer;

    private ObjectId currentInventoryId;

    @FXML
    private TextField username;

    @FXML
    private void addUser(){
        System.out.println("hwere...");
        if (this.currentInventoryId == null ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Select an inventory first by clicking the show button.");
            alert.showAndWait();
            return;
        }
        shareInventoryToUser(this.currentInventoryId, username.getText());
        loadUsersWithAccess(currentInventoryId);
    }

    private void shareInventoryToUser(ObjectId inventoryId, String username){
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
    }


    private void removeUser(ObjectId inventoryId, String username){
        Document user = new Database().getConnection("Users").find(new Document("username", username)).first();
        MongoCollection<Document> inventoryDB = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id",inventoryId);
        Bson update = Updates.pull("sharedTo", username);
        Iterable<String> sharedTo= (Iterable<String>) inventoryDB.find(filter).first().get("sharedTo");
        inventoryDB.findOneAndUpdate(filter, update);
        loadUsersWithAccess(inventoryId);
    }

    private void loadUsersWithAccess(ObjectId id){
        this.usersWithAccessContainer.getChildren().clear();
        MongoCollection<Document> inventoryData = new Database().getConnection("Inventories");
        currentInventoryId = id;
        Iterable<String> users = (Iterable<String>)inventoryData.find(new Document("_id", id)).first().get("sharedTo");
        int count = 0;
        for (String user: users){
            Label lbl = new Label(user);
            Button removeBtn = new Button("Remove user");

            removeBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    removeUser(currentInventoryId,user);
                }
            });

            usersWithAccessContainer.getChildren().addAll(lbl, removeBtn);
            count++;
        };
        if (count ==0){
            usersWithAccessContainer.getChildren().add(new Label("Only you have access."));
        }
    }


    private void listInventories(){
        Iterable<ObjectId> owned = (Iterable<ObjectId>) userData.get("ownedInventories");
        owned.forEach(id -> {
            Document data =new Database().getConnection("Inventories").find(new Document("_id", id)).first();
            HBox container = new HBox();
            container.getChildren().add(new Label((String) data.get("inventoryName")));

            Button showBtn = new Button("Show users");
            container.getChildren().add(showBtn);
            // connect to a list view in the view

            // load the users with access
            showBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    loadUsersWithAccess(id);
                }
            });

            container.setPadding(new Insets(5, 5, 5, 5));
            container.setSpacing(30);
            showBtn.setCursor(Cursor.HAND);


            toShareContainer.getChildren().add(container);
        });
    }

}
