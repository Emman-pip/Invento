package com.main.invento.controllers;

import com.main.invento.Main;
import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.InventoryLogger;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
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
        controller.setParent(itemsParentVbox);
        controller.setUsername(this.username);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    };


    public static  boolean deleteItem(ObjectId  itemId, ObjectId inventoryId){
        // search how to delete items in an array in mongo db(by id)
        Bson filter = Filters.eq("_id", inventoryId);
        Bson recordPull = Filters.eq("itemId", itemId);
        Bson update = Updates.pull("items", recordPull);

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setContentText("This action will permamnently delete the item");
        confirmationAlert.showAndWait();
        if (confirmationAlert.getResult() != ButtonType.OK){
            return false;
        }
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        db.findOneAndUpdate(filter, update);
        return true;

    }
    @FXML
    private VBox itemsParentVbox;

    public static void loadItems(VBox parent, Document inventoryData){
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
            Button deleteBtn = new Button("Delete item");
            informationContainer.getChildren().add(deleteBtn);
            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // ObjectId  itemId, ObjectId inventoryId, String username
                    boolean res  = InventoryPageController.deleteItem((ObjectId) item.get("itemId"), (ObjectId) inventoryData.get("_id"));
                    // String username, ObjectId inventoryId, ObjectId itemId, String itemName
                    if (res){
                        InventoryLogger.deleteItem(null, (ObjectId) inventoryData.get("_id"), (ObjectId) item.get("itemId"), (String) item.get("itemName"));
                        Document newData = new Database().getConnection("Inventories").find(new Document("_id", (ObjectId) inventoryData.get("_id"))).first();
                        InventoryPageController.loadItems(parent, newData);
                    }

                }
            });
            TitledPane pane = new TitledPane();
            pane.setText("Information");
            pane.setContent(informationContainer);
            pane.setExpanded(false);
            mainContainer.getChildren().add(pane);
            parent.getChildren().add(mainContainer);
            System.out.println();
        }
    }
    // REPORTS (sales)
    //    TODO report sales
    //    TODO report losses
    // Update stock
    //    TODO addNewStock
    //    TODO update category
    //    TODO change capital
    //    TODO correct records (optional)
    @FXML
    private void openManageItems() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/item-operations-view.fxml"));
        Stage stage =  new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

// DONE deleteItem
// TODO   add validators (and change the shitty percentage) - to add item
}

// DONE displayItem
// TODO item operations
//    TODO report sales
//    TODO report losses
//    TODO addNewStock
//    TODO update category
//    TODO change capital

//    TODO correct records (optional)

// after this, can add the analytics
// then after that, do the generate reports
