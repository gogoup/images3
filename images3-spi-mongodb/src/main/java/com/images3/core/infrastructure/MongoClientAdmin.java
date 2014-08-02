package com.images3.core.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoClientAdmin {

    private Properties config;
    private MongoClient mongoClient;
    
    public MongoClientAdmin() {
        loadConfigProperties();
        initMongoClient();
    }
    
    private void loadConfigProperties() {
        config = new Properties();
        InputStream in = getClass().getResourceAsStream("/config.properties");
        try {
            config.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void initMongoClient() {
        String url = config.getProperty("mongodb.url");
        int port = Integer.valueOf(config.getProperty("mongodb.port"));
        //String username = config.getProperty("mongodb.username");
        //String password = config.getProperty("mongodb.password");
        try {
            this.mongoClient = new MongoClient(url, port);
            initCollections();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } 
    }
    
    public DB getDatabase() {
        return mongoClient.getDB("images3");
    }
    
    private void initCollections() {
        Set<String> collNames = getDatabase().getCollectionNames();
        if (!collNames.contains("ImagePlant")) {
            initImagePlant();
        }
        if (!collNames.contains("Template")) {
            initTemplate();
        }
        if (!collNames.contains("Image")) {
            initImage();
        }
        if (!collNames.contains("Version")) {
            initVersion();
        }
    }
    
    private void initImagePlant() {
        DBCollection coll = getDatabase().getCollection("ImagePlant");
        coll.createIndex(new BasicDBObject("id", "hashed"), new BasicDBObject().append("dropDups", false));
        coll.createIndex(new BasicDBObject("name", 1), new BasicDBObject("unique", true).append("dropDups", false));
        coll.createIndex(new BasicDBObject("creationTime", 1));
    }
    
    private void initTemplate() {
        DBCollection coll = getDatabase().getCollection("Template");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("id", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("name", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("isArchived", 1));
    }
    
    private void initImage() {
        DBCollection coll = getDatabase().getCollection("Image");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("id", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
    }
    
    private void initVersion() {
        DBCollection coll = getDatabase().getCollection("Version");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("imageId", 1).append("templateId", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
    }
    
}
