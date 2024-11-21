package com.main.invento.models;

import com.main.invento.utilityClasses.Database;
import com.main.invento.utilityClasses.Encryptor;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;

public class UserAuthorizationModel {
    public static boolean userExists(String username){
        Document userData = new Document();
        userData.append("username", username);
        Iterable<Document> data = new Database().getConnection("Users").find(userData);
        boolean flag = false;
        int count = 0;
        for (Document d : data){
            count++;
        }
        return count > 0;
    }
    public void addUser(String username, String email, String organization, String password) throws NoSuchAlgorithmException {
        MongoCollection<Document> db = new Database().getConnection("Users");
        Document newUser = new UserModel(username, email, organization, Encryptor.SHA256(password)).getUser();
        db.insertOne(newUser);
    }

}
