package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.InventoryLogger;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
    private VBox parent;

    public void setParent(VBox parent) {
        this.parent = parent;
    }

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
    private GridPane columnsContainer;
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private void loadForm(){
        ArrayList<String> columns = (ArrayList<String>) this.inventoryData.get("columns");

        ArrayList<TextField> textfields = new ArrayList<>();
        int rowCount = 0;
        for (String column : columns) {
            Label lbl = new Label(column + ": ");
            TextField inputField = new TextField();
            textfields.add(inputField);
            GridPane.setConstraints(lbl, 0, rowCount);
            GridPane.setConstraints(inputField, 1, rowCount);
            columnsContainer.getChildren().addAll(lbl, inputField);
            columnsContainer.setVgap(10);
            rowCount++;
        }

        Button submit = new Button("Add item");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Document newItem = new Document();
                newItem.put("itemId", new ObjectId());
                // try collating the data into a document then submitting it after (if valid)
                for (int i = 0; i < textfields.size(); i++){
                    String str = textfields.get(i).getText();
                    if (isNumeric(str)){
                        newItem.put(columns.get(i), Integer.parseInt(str));
                        continue;
                    }
                    newItem.put(columns.get(i), str);
                }
                // UPDATE HERE
                updateItems(newItem);
                Stage oldStage = (Stage) submit.getScene().getWindow();
                oldStage.close();
                // VBox parent, Document inventoryData
                Document inventoryData = new Database().getConnection("Inventories").find(new Document("_id", inventoryId)).first();
                InventoryPageController.loadItems(parent, inventoryData);
            }
        });
        HBox btnContainer = new HBox();
        btnContainer.getChildren().add(submit);
        btnContainer.setAlignment(Pos.CENTER);
        UserDashboardController.setButtonAnimation(submit);
        GridPane.setConstraints(btnContainer, 0, rowCount, 2, 1);
        columnsContainer.getChildren().add(btnContainer);
    }
}
