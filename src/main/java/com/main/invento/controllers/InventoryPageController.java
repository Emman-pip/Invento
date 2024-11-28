package com.main.invento.controllers;

import com.main.invento.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InventoryPageController {
    private Document inventoryData;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setInventoryData(Document inventoryData) {
        this.inventoryData = inventoryData;
        inventoryName.setText(
                "Inventory: " + this.inventoryData.get("inventoryName")
        );
        loadItems(itemsParentVbox, inventoryData);
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

    @FXML
    private void addItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/add-item-view.fxml"));
        Parent root = loader.load();
        AddItemController controller = loader.getController();
        controller.setInventoryId((ObjectId) this.inventoryData.get("_id"));
        controller.setUsername(this.username);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    };

    @FXML
    private VBox itemsParentVbox;

    private static void loadItems(VBox parent, Document inventoryData){
        Iterable<String> columns = (Iterable<String>) inventoryData.get("columns");
        Iterable<Document> items = (Iterable<Document>) inventoryData.get("items");
        String[] defaultValues = {"itemName",  "categories"};
        List<String> listOfDefaultValues = Arrays.asList(defaultValues);
        parent.getChildren().clear();
        for (Document item : items) {
            VBox mainContainer = new VBox();
            mainContainer.setStyle("-fx-background-color: white;");
            VBox informationContainer = new VBox();

            HBox itemNameAndCategory = new HBox();
            itemNameAndCategory.getChildren().addAll(new Label((String)item.get("itemName") ), new Label((String)item.get("category")));
            mainContainer.getChildren().add(itemNameAndCategory);

            for (String column : columns) {
                if (listOfDefaultValues.contains(column)){
                    continue;
                }
                informationContainer.getChildren().add(new Label(column + ": " + item.get(column)));
            }
            TitledPane pane = new TitledPane();
            pane.setText("Information");
            pane.setContent(informationContainer);
            pane.setExpanded(false);
            mainContainer.getChildren().add(pane);
            parent.getChildren().add(mainContainer);
            System.out.println();
        }
    }
}

// TODO HERE
// TODO addItem - add validators (and change the shitty percentage)
// DONE displayItem
// TODO deleteItem
// TODO item operations
//    TODO report sales
//    TODO report losses
//    TODO correct records
//    TODO change capital
//    TODO addNewStock
//    TODO update category

// after this, can add the analytics
// then after that, do the generate reports
