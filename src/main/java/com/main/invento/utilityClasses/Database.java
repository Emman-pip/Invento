package com.main.invento.utilityClasses;


import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Database {
        private String dbURI = "mongodb://emman-pip:108996@127.0.0.1:27017/";
        public MongoCollection<Document> getConnection(String collectionName){
            ConnectionString uri = new ConnectionString(this.dbURI);
            MongoClient mongo = MongoClients.create(uri);
            MongoDatabase db = mongo.getDatabase("invento");
            MongoCollection<Document> data =  db.getCollection(collectionName);
            return data;
        }
}
