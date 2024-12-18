package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.InventoryLogger; import com.mongodb.client.MongoCollection; import com.mongodb.client.model.Filters; import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
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

    @FXML
    private Button updateBtn;

    @FXML
    private Button addBtn;
    public void initialize() {
        columnsScrollPane.setFitToWidth(true);
        ShareInventoryController.addBtnStyle(addBtn);
        ShareInventoryController.addBtnStyle(updateBtn);
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
        newName.setText((String) inventoryData.get("inventoryName"));
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
        if (newCol.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Can't set an empty named column");
            alert.showAndWait();
            return;
        }
        String columnName = newCol.getText();
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", this.inventoryId);
        Bson update = Updates.push("columns", columnName);
        InventoryLogger.addColumns(this.username, this.inventoryId, columnName);
        db.findOneAndUpdate(filter, update);
        newCol.setText("");
        loadColumns();
    }

    private void loadColumns() {
        columns.getChildren().clear();
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        this.inventoryData = new Database().getConnection("Inventories").find(new Document("_id", inventoryId)).first();
        Iterable<String> cols = (Iterable<String>) inventoryData.get("columns");
        String[] defaultValues = {"itemName", "units", "description", "capitalPerUnit", "category"};

        for (String col : cols) {
            Label lbl = new Label(col);
            Button deleteBtn = new Button("Delete column");
            ShareInventoryController.addBtnStyle(deleteBtn);
            if (Arrays.asList(defaultValues).contains(col)) {
                deleteBtn.setDisable(true);
            }

            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("This action will delete column: " + col);
                    alert.showAndWait();
                    if (ButtonType.OK == alert.getResult()){
                        deleteColumns(col);
                        InventoryLogger.deleteColumns(username, inventoryId, col);
                        loadColumns();
                    }
                }
            });

            BorderPane pane = new BorderPane();
            pane.setCenter(lbl);
            pane.setRight(deleteBtn);
            columns.getChildren().addAll(pane);
        }
    }
}