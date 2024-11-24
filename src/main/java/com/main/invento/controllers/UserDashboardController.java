package com.main.invento.controllers;

import com.main.invento.Main;
import com.main.invento.utilityClasses.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDashboardController {
    private String username;
    private Document userData;

    @FXML
    private Label welcome;

    public void setUsername(String username) {
        this.username = username;
        welcome.setText("Welcome, " + this.username);
        loadUserData();
        loadOwnedInventories();
        loadSharedInventories();
    }

    private void loadUserData(){
        // retrieve and assign the user data to the userData attribute
        this.userData = new Database().getConnection("Users").find(new Document("username", username)).first();
//      analytics.getChildren().add(new Label(Arrays.toString(new ArrayList[]{(ArrayList<String>) this.userData.get("ownedInventories")})));
//      analytics.getChildren().add(new Label("" + userData.get("_id")));
      // function to get the keys of a document
//      System.out.println(userData.keySet());
    }

    @FXML
    private HBox analytics;

    @FXML
    private ImageView inventoryIcon;

    @FXML
    private ScrollPane sharedInventoriesParent;
    @FXML
    public void initialize(){
        this.ownedInventoriesParent.setFitToWidth(true);
        this.sharedInventoriesParent.setFitToWidth(true);
    }

    @FXML
    private void createNewInventory() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/create-inventory.fxml"));
        Parent root = loader.load();
        CreateInventoryController controller = (CreateInventoryController) loader.getController();
        controller.setUsername((String) this.userData.get("username"));
        controller.setParent(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // loadInventories
    @FXML
    private VBox ownedInventoriesContainer;
    @FXML
    private ScrollPane ownedInventoriesParent;;
    @FXML
    public void loadOwnedInventories(){
        MongoCollection<Document> db = new Database().getConnection("Users");
        Document user = db.find(new Document("username", this.username)).first();
        ArrayList<ObjectId> listOfOwnedInventories = (ArrayList<ObjectId>) user.get("ownedInventories");
        this.ownedInventoriesContainer.getChildren().clear();
        listOfOwnedInventories.forEach(id -> {
            createInventoryDisplay(id, ownedInventoriesContainer);
        });
    }

    private void openInventory(ObjectId id){
        System.out.println("inventory will open now " + id);
    }

    @FXML
    private void openShareInventory() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/share-inventory-view.fxml"));
        Parent loaded = loader.load();
        ShareInventoryController controller = loader.getController();
        controller.setUserData(new Database().getConnection("Users").find(new Document("username", username)).first());

        Stage stage = new Stage();
        stage.setScene(new Scene(loaded));
        stage.show();

    }

    private void updateInventoryBtn(Document item) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/update-inventory-view.fxml"));
        Parent root = loader.load();
        UpdateInventoryController controller = loader.getController();
        controller.setUsername(username);
        controller.setInventoryId((ObjectId) item.get("_id"));
        controller.setParent(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        System.out.println("hwer");
    }

    // create a method that will return a Hbox, with size and shit
    private void createInventoryDisplay(ObjectId id, Pane parent){
        MongoCollection inventoryData = new Database().getConnection("Inventories");
        Bson filter = Filters.and(Filters.eq("_id", id), Filters.eq("isDeleted", false));
        Document item = (Document) inventoryData.find(filter).first();
        if (item == null){
            return;
        }
        HBox container = new HBox();

        Button updateBtn = new Button("Update");
        Label lbl = new Label((String)item.get("inventoryName"));

        Button deleteBtn = new Button("Delete Inventory");

        Iterable<ObjectId> owned = (Iterable<ObjectId>) new Database().getConnection("Users").find(new Document("username", username)).first().get("ownedInventories");
        boolean isInside= false;
        for (ObjectId inventory: owned){
            if (inventory.equals(item.get("_id"))){
                isInside = true;
            }
        }

        if (!isInside){
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
        }

        container.setStyle("-fx-background-color: white; -fx-padding: 10px;");
        container.setCursor(Cursor.HAND);
        container.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                openInventory((ObjectId) item.get("_id"));
            }
        });

        updateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    updateInventoryBtn(item);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MongoCollection<Document> db = new Database().getConnection("Inventories");
                Bson filter = Filters.eq("_id", item.get("_id"));
                Bson update  = Updates.set("isDeleted", true);
                db.findOneAndUpdate(filter, update);
                loadOwnedInventories();
            }
        });


        container.getChildren().addAll(lbl, updateBtn, deleteBtn);
        parent.getChildren().add(container);
    }
    @FXML
    private VBox sharedInventoriesContainer;
    // loadSharedInventories
    private void loadSharedInventories(){
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Iterable<Document> data = db.find(new Document("sharedTo", username));
        data.forEach(
                inventory -> {
                    createInventoryDisplay((ObjectId) inventory.get("_id"), sharedInventoriesContainer);
                }
        );


    }

    @FXML
    private void logout() throws IOException{
        Stage oldstage = (Stage)this.analytics.getScene().getWindow();
        oldstage.close();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxmls/login-view.fxml"));
        Scene scene =  new Scene(loader.load());
        Stage  stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Signup");
        stage.setResizable(false);
        stage.show();
    }
    // loadInventoryRanks(): void
    //
    // loadInventorySales(): void
    //
    // loadPieChart(): void
    //
    // loadAlerts(): void
}
