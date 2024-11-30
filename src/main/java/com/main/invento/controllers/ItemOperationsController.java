package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.InventoryLogger;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.util.List;

import static com.main.invento.controllers.InventoryPageController.loadItems;

public class ItemOperationsController {

    protected Document InventoryData;

    public void setInventoryData(Document inventoryData) {
        this.InventoryData = inventoryData;
        this.inventoryName.setText((String) new Database().getConnection("Inventories").find(new Document("_id", inventoryData.get("_id"))).first().get("inventoryName"));
    }

    private Document itemChosen;

    public void setItemChosen(Document itemChosen) {
        this.itemChosen = itemChosen;
    }

    // TODO add to log when reported
        // TODO minus to units on hand the number of units
    // TODO
        //
    // TODO


    @FXML
    private Label itemName;
    @FXML
    private Label inventoryName;
    @FXML
    private TitledPane  searchResults;
    @FXML
    private ScrollPane searchResultsScroll;
    @FXML
    private TextField searchInp;
    @FXML
    private VBox searchResultsParent;

    public void initialize(){
        searchResults.setExpanded(false);
        searchResultsScroll.setFitToWidth(true);

    }

    private void selectItem(Document item){
        setItemChosen(item);
        if (this.itemChosen != null){
            itemName.setText((String)item.get("itemName"));
        } else{
            itemName.setText("no selected");
        }
        searchResults.setExpanded(false);
    }

    // TODO search
    // by keypress, update the resultBox
    // if clicked result, close the titled pane, set the item name and ID to the clicked
    @FXML
    private void searchAction(){
        searchResultsParent.getChildren().clear();
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Document data = db.find(new Document("_id", InventoryData.get("_id"))).first();
        Iterable<Document>  results = (Iterable<Document>) data.get("items");
//        Bson filter = Filters.eq("itemName", new Document("$regex", ".*"+searchInp.getText()+".* "));
//        Iterable<Document> results = (Iterable<Document>) db.find(filter);
        for (Document result : results) {
            String resString = (String) result.get("itemName");
            if (resString.contains(searchInp.getText())){
                System.out.println((String) result.get("itemName"));
                Label lbl = new Label((String) result.get("itemName"));
                searchResultsParent.getChildren().add(lbl);
                lbl.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        selectItem(result);
                    }
                });

            }
        }

        searchResults.setExpanded(true);
    }

    @FXML
    private TextField unitsSold;

    @FXML
    private TextField totalSales;

    @FXML
    private void reportSale(){
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        int soldUnits = Integer.parseInt(unitsSold.getText());
        int sales = Integer.parseInt(this.totalSales.getText());

        // minus from total units
        Bson filter = Filters.and(
                Filters.eq("_id", this.InventoryData.get("_id")),
                Filters.eq("items.itemId", this.itemChosen.get("itemId")));
        int oldUnits = Integer.parseInt(String.valueOf(itemChosen.get("units"))) ;
        int res = oldUnits - soldUnits;
        System.out.println(res);
        if (res < 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Not enough units");
            alert.showAndWait();
            return;
        }
        Bson update = Updates.inc("items.$.units", -soldUnits);
        //String reportType, ObjectId inventoryId, ObjectId itemId, String itemName, int sales, int unitsSold
        db.updateOne(filter, update);
        // log it to the inventory log
        InventoryLogger.report("sales", (ObjectId) this.InventoryData.get("_id"), (ObjectId) itemChosen.get("_id"), (String) itemChosen.get("itemName"), sales, soldUnits);
        selectItem(null);
        unitsSold.setText("");
        totalSales.setText("");
        searchResults.setExpanded(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Sale reported");
        alert.showAndWait();
        reloadInventoryPage();
    }

    private void reloadInventoryPage(){
        this.InventoryData = new Database().getConnection("Inventories").find(new Document("_id", this.InventoryData.get("_id"))).first();
        InventoryPageController.loadItems(itemsParentVbox, this.InventoryData);
    }
    private VBox itemsParentVbox;

    public void setItemsParentVbox(VBox itemsParentVbox) {
        this.itemsParentVbox = itemsParentVbox;
    }
}
