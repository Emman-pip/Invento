package com.main.invento.models;

import com.mongodb.BasicDBObject;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private String username;
    private String email;
    private String org;
    private String password;
    private List<BasicDBObject> ownedInventories = new ArrayList<>();
    private Document logs = new Document();

    public UserModel (String username, String email, String organization, String password){
        this.username = username;
        this.email = email;
        this.org = organization;
        this.password =  password;
        // String ownedInventoriesss
        ObjectId id = new ObjectId();
        this.logs.put("id", id);
        this.logs.put("timestamp", id.getTimestamp());
        this.logs.put("loginTime", id.getTimestamp());
        this.logs.put("description", "Account creation");
    }
    public Document getUser(){
        Document newUser = new Document();
        newUser.append("username", this.username);
        newUser.append("email", this.email);
        newUser.append("organization", this.org);
        newUser.append("password", this.password);
        newUser.append("ownedInventories", this.ownedInventories);
        newUser.append("logs", this.logs);
        return newUser;
    }
}
