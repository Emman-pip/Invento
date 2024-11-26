package com.main.invento.models;

import org.bson.Document;

public class ItemModel {
    private Document item = new Document();
    // ["itemName", "units", "description", "capitalPerUnit"]
    public ItemModel(String itemName, Double units , String description, double capitalPerUnit){
        item.put("itemName", itemName)      ;
        item.put("units", units)            ;
        item.put("description", description);
        item.put("capitalPerUnit", capitalPerUnit);
    }
}
