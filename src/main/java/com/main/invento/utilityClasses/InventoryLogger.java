package com.main.invento.utilityClasses;

import com.main.invento.models.InventoryLogsModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class InventoryLogger {
    public static void createdInventory(String username, ObjectId inventoryId){
        Document log = new InventoryLogsModel(null, "Create Inventory", 0, 0).getInventoryLog();
        log.put("username", username);
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", inventoryId);
        Bson update = Updates.push("logs", log);
        db.findOneAndUpdate(filter, update);
    }
    public static void accessedInventory(String username, ObjectId inventoryId){
        Document log = new InventoryLogsModel(null, "accessed inventory", 0, 0).getInventoryLog();
        log.put("username", username);
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", inventoryId);
        Bson update = Updates.push("logs", log);
        db.findOneAndUpdate(filter, update);
    }
    public static void addColumns(String username, ObjectId inventoryId, String columnName){
        Document log = new InventoryLogsModel(null, "added column:" + columnName, 0, 0).getInventoryLog();
        log.put("username", username);
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", inventoryId);
        Bson update = Updates.push("logs", log);
        db.findOneAndUpdate(filter, update);

    }
    public static void deleteColumns(String username, ObjectId inventoryId, String columnName){
        Document log = new InventoryLogsModel(null, "Deleted column: " + columnName, 0, 0).getInventoryLog();
        log.put("username", username);
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", inventoryId);
        Bson update = Updates.push("logs", log);
        db.findOneAndUpdate(filter, update);

    }
    public static void deleteInventory(String username, ObjectId inventoryId){
        Document log = new InventoryLogsModel(null, "deleted inventory", 0, 0).getInventoryLog();
        log.put("username", username);
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", inventoryId);
        Bson update = Updates.push("logs", log);
        db.findOneAndUpdate(filter, update);
    }

    public static void sharedInventory(String username, ObjectId inventoryId, String sharedTo){
        Document log = new InventoryLogsModel(null, "Shared inventory to: " + sharedTo, 0, 0).getInventoryLog();
        log.put("username", username);
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", inventoryId);
        Bson update = Updates.push("logs", log);
        db.findOneAndUpdate(filter, update);
    }
    public static void unsharedInventory(String username, ObjectId inventoryId, String sharedTo){
        Document log = new InventoryLogsModel(null, "Unshared inventory to: " + sharedTo, 0, 0).getInventoryLog();
        log.put("username", username);
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Bson filter = Filters.eq("_id", inventoryId);
        Bson update = Updates.push("logs", log);
        db.findOneAndUpdate(filter, update);
    }
}
