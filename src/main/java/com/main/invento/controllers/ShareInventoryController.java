package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;
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

    private void listInventories(){
        Iterable<ObjectId> owned = (Iterable<ObjectId>) userData.get("ownedInventories");
        owned.forEach(id -> {
            Document data =new Database().getConnection("Inventories").find(new Document("_id", id)).first();
            HBox container = new HBox();
            container.getChildren().add(new Label((String) data.get("inventoryName")));

            Button showBtn = new Button("Show users");
            container.getChildren().add(showBtn);
            // connect to a list view in the view

            Button shareBtn = new Button("Share Inventory");
            container.getChildren().add(shareBtn);
            // connect to a function that will modify the database and reload the view

            container.setPadding(new Insets(5, 5, 5, 5));
            container.setSpacing(30);
            showBtn.setCursor(Cursor.HAND);
            shareBtn.setCursor(Cursor.HAND);


            toShareContainer.getChildren().add(container);
        });
    }

    @FXML
    usersWithAccessContainer
}
