package com.main.invento.models;

import org.bson.Document;
import org.bson.types.ObjectId;

public class InventoryLogsModel {
    private Document inventoryLog = new Document();
    public InventoryLogsModel(ObjectId itemId, String activity, double sales, double unitsSold){
        ObjectId id = new ObjectId();
        this.inventoryLog.put("_id", id);
        this.inventoryLog.put("itemId", itemId);
        this.inventoryLog.put("activity", activity);
        this.inventoryLog.put("sales", sales);
        this.inventoryLog.put("timestamp", id.getTimestamp());
        this.inventoryLog.put("unitsSold", unitsSold);
    }

    public Document getInventoryLog() {
        return inventoryLog;
    }
}
