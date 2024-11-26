package com.main.invento.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.bson.Document;

public class InventoryPageController {
    private Document inventoryData;

    public void setInventoryData(Document inventoryData) {
        this.inventoryData = inventoryData;
        inventoryName.setText(
                "Inventory: " + this.inventoryData.get("inventoryName")
        );
        loadItems();
    }

    @FXML
    private AnchorPane itemsContainer;

    @FXML
    private ScrollPane itemsParentContainer;

    @FXML
    private Label inventoryName;

    public void initialize(){
        itemsParentContainer.setFitToWidth(true);
    }

    private void loadItems(){
        Iterable<String> columns = (Iterable<String>) inventoryData.get("columns");
        Iterable<Document> items = (Iterable<Document>) inventoryData.get("items");
        for (Document item : items) {
            for (String column : columns) {
                System.out.print(item.get(column) + " ");
            }
            System.out.println();
        }
    }
}
