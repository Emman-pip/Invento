package com.main.invento.controllers;

import com.main.invento.Main;
import com.main.invento.utilityClasses.Database;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    public void initialize(){
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
        MongoCollection inventoryData = new Database().getConnection("Inventories");
        this.ownedInventoriesParent.setFitToWidth(true);
        this.ownedInventoriesContainer.getChildren().clear();
        listOfOwnedInventories.forEach(id -> {
            Bson filter = Filters.and(Filters.eq("_id", id), Filters.eq("isDeleted", false));
            Document item = (Document) inventoryData.find(filter).first();
            if (item == null){
                return;
            }
            HBox container = new HBox();
            container.getChildren().add(new Label((String)item.get("inventoryName")));
            container.setStyle("-fx-background-color: white; -fx-padding: 10px;");
            container.setCursor(Cursor.HAND);
            container.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    openInventory((ObjectId) item.get("_id"));
                }
            });
            ownedInventoriesContainer.getChildren().add(container);
        });
    }

    private void openInventory(ObjectId id){
        System.out.println("inventory will open now " + id);
    }

    @FXML
    private void openShareInventory(){

    }


    // loadSharedInventories
    // createNewInventory(): void
    //
    // LoadInventories(): void
    //
    // loadSharedInventories(): void
    //
    // loadInventoryRanks(): void
    //
    // loadInventorySales(): void
    //
    // loadPieChart(): void
    //
    // loadAlerts(): void
    // opeinventory(): void
    //
    // openShareInventory():void
}
