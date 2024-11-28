package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.InventoryLogger;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class AddItemController {
    private ObjectId inventoryId;
    private Document inventoryData;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInventoryId(ObjectId inventoryId) {
        this.inventoryId = inventoryId;
        this.inventoryData = new Database().getConnection("Inventories").find(new Document("_id", inventoryId)).first();
        loadForm();
    }


    private void updateItems(Document newItem){
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", this.inventoryId);
        Bson update = Updates.push("items", newItem);
        db.findOneAndUpdate(filter, update);
        InventoryLogger.addItem(this.username, (ObjectId) this.inventoryData.get("_id"), (ObjectId) newItem.get("itemID"), (String) newItem.get("itemName"));
    }

    @FXML
    private VBox columnsContainer;

    private void loadForm(){
        ArrayList<String> columns = (ArrayList<String>) this.inventoryData.get("columns");

        ArrayList<TextField> textfields = new ArrayList<>();
        for (String column : columns) {
            HBox container = new HBox();
            Label lbl = new Label(column + ": ");
            TextField inputField = new TextField();
            textfields.add(inputField);
            container.getChildren().addAll(lbl, inputField);
            columnsContainer.getChildren().add(container);
        }

        Button submit = new Button("Add item");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Document newItem = new Document();
                newItem.put("itemId", new ObjectId());
                // try collating the data into a document then submitting it after (if valid)
                for (int i = 0; i < textfields.size(); i++){
                    newItem.put(columns.get(i), textfields.get(i).getText());
                }
                // UPDATE HERE
                updateItems(newItem);
                Stage oldStage = (Stage) submit.getScene().getWindow();
                oldStage.close();
            }
        });
        columnsContainer.getChildren().add(submit);
    }
}
