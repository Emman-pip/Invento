package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.InventoryLogger; import com.mongodb.client.MongoCollection; import com.mongodb.client.model.Filters; import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Arrays;

public class UpdateInventoryController {
    private String username;
    private ObjectId inventoryId;
    private Document inventoryData;
    private UserDashboardController parent;

    public void initialize() {
        columnsScrollPane.setFitToWidth(true);
        columnsContainer.setExpanded(false);
    }

    public void setParent(UserDashboardController parent) {
        this.parent = parent;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInventoryId(ObjectId inventoryId) {
        this.inventoryId = inventoryId;
        inventoryData = new Database().getConnection("Inventories").find(new Document("_id", inventoryId)).first();
        this.inventoryName.setText("Update inventory:" + (String) inventoryData.get("inventoryName"));
        loadColumns();
    }

    @FXML
    private Label inventoryName;

    @FXML
    private TextField newName;

    @FXML
    private void updateName() {
        if (newName.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid inventory name");
            alert.showAndWait();
            return;
        }
        Bson filter = Filters.eq("_id", this.inventoryId);
        Bson update = Updates.set("inventoryName", newName.getText());
        MongoCollection<Document> db = new Database().getConnection("Inventories");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("This will change the name of the inventory");
        alert.showAndWait();

        if (alert.getResult() != ButtonType.OK) {
            return;
        }
        db.findOneAndUpdate(filter, update);
        UserDashboardController parentScreen = parent;
        parentScreen.loadOwnedInventories();

        Stage stage = (Stage) newName.getScene().getWindow();
        stage.close();

        Alert done = new Alert(Alert.AlertType.INFORMATION);
        done.setContentText("Updated the name of the inventory.");

        done.showAndWait();

    }

    @FXML
    private ScrollPane columnsScrollPane;
    @FXML
    private TitledPane columnsContainer;
    @FXML
    private VBox columns;

    private void deleteColumns(String columnName) {
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", this.inventoryId);
        Bson update = Updates.pull("columns", columnName);
        db.findOneAndUpdate(filter, update);
    }

    @FXML
    private TextField newCol;

    @FXML
    private void addColumn() {
        String columnName = newCol.getText();
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", this.inventoryId);
        Bson update = Updates.push("columns", columnName);
        InventoryLogger.addColumns(this.username, this.inventoryId, columnName);
        db.findOneAndUpdate(filter, update);
        loadColumns();
    }

    private void loadColumns() {
        columns.getChildren().clear();
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        this.inventoryData = new Database().getConnection("Inventories").find(new Document("_id", inventoryId)).first();
        Iterable<String> cols = (Iterable<String>) inventoryData.get("columns");
        String[] defaultValues = {"itemName", "units", "description", "capitalPerUnit"};

        for (String col : cols) {
            Label lbl = new Label(col);
            Button deleteBtn = new Button("Delete column");
            if (Arrays.asList(defaultValues).contains(col)) {
                deleteBtn.setDisable(true);
            }
            HBox container = new HBox();

            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    deleteColumns(col);
                    InventoryLogger.deleteColumns(username, inventoryId, col);
                    loadColumns();
                }
            });

            container.getChildren().addAll(lbl, deleteBtn);
            columns.getChildren().addAll(container);
        }
    }
}