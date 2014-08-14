package com.images3.core.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.core.infrastructure.spi.TemplateAccess;
import com.images3.core.infrastructure.spi.VersionAccess;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class ObjectSegmentAccessProvider {
    
    private static final String DBNAME = "images3";
    
    private Properties config;
    private MongoClient mongoClient;
    private MongoDBObjectMapper objectMapper;
    
    public ObjectSegmentAccessProvider(String pathToConfig) {
        loadConfigProperties(pathToConfig);
        initMongoClient();
        objectMapper = new MongoDBObjectMapper();
    }
    
    private void loadConfigProperties(String pathToConfig) {
        config = new Properties();
        try {
            InputStream in = new FileInputStream(new File(pathToConfig));
            config.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImagePlantAccess getImagePlantAccess() {
        return new ImagePlantAccessImplMongoDB(mongoClient, DBNAME,  objectMapper);
    }
    
    public ImageAccess getImageAccess() {
        return new ImageAccessImplMongoDB(mongoClient, DBNAME, objectMapper);
    }
    
    public TemplateAccess getTemplateAccess() {
        return new TemplateAccessImplMongoDB(mongoClient, DBNAME, objectMapper);
    }
    
    public VersionAccess getVersionAccess() {
        return new VersionAccessImplMongoDB(mongoClient, DBNAME, objectMapper);
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
    
    private void initCollections() {
        DB images3DB = mongoClient.getDB(DBNAME);
        Set<String> collNames = images3DB.getCollectionNames();
        if (!collNames.contains("ImagePlant")) {
            initImagePlant(images3DB);
        }
        if (!collNames.contains("Template")) {
            initTemplate(images3DB);
        }
        if (!collNames.contains("Image")) {
            initImage(images3DB);
        }
        if (!collNames.contains("Version")) {
            initVersion(images3DB);
        }
    }
    
    private void initImagePlant(DB db) {
        DBCollection coll = db.getCollection("ImagePlant");
        coll.createIndex(new BasicDBObject("id", "hashed"), new BasicDBObject().append("dropDups", false));
        coll.createIndex(new BasicDBObject("name", 1), new BasicDBObject("unique", true).append("dropDups", false));
        coll.createIndex(new BasicDBObject("creationTime", 1));
    }
    
    private void initTemplate(DB db) {
        DBCollection coll = db.getCollection("Template");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("id", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("name", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("isArchived", 1));
    }
    
    private void initImage(DB db) {
        DBCollection coll = db.getCollection("Image");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("id", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
    }
    
    private void initVersion(DB db) {
        DBCollection coll = db.getCollection("Version");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("imageId", 1).append("templateId", 1), 
                new BasicDBObject("unique", true).append("dropDups", true));
    }
}
