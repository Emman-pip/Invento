package com.main.invento.models;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryModel {
    private ObjectId inventoryId = new ObjectId();

    public ObjectId getInventoryId() {
        return inventoryId;
    }

    private Document inventory = new Document();

    public InventoryModel(String inventoryName, String username){
        inventory.put("_id", inventoryId);
        inventory.put("inventoryName", inventoryName);
        inventory.put("username", username);
        inventory.put("isDeleted", false);
        inventory.put("sharedTo", Arrays.asList());
        inventory.put("items", Arrays.asList());
        inventory.put("columns", Arrays.asList("itemName", "units", "description", "capitalPerUnit"));
    }

    public void initializeModel(){
        inventory.put("logs", Arrays.asList());
    }

    public Document getInventory(){
        return this.inventory;
    }

//        {
//            "itemId": objectId(),
//                "itemName": "name",
//                "units": 0,
//                "description": null,
//                "pricePerUnit": 0.00,
//                "standardProfitPerUnit": 0.1,
//                "dateAdded": timestamp,
//                "dataUpdated": timestamp,
//                "isDeleted": false,
//                "history": {
//            {
//                "timestamp": timestamp, "pricePerUnit": 0.00,
//                    "standardProfitPerUnit": 0.00
//            }
}
