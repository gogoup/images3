package com.images3.core.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageMetricsService;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.core.infrastructure.spi.TemplateAccess;
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
        int pageSize = Integer.valueOf(config.getProperty("imageplant.page.size"));
        return new ImagePlantAccessImplMongoDB(mongoClient, DBNAME,  objectMapper, pageSize);
    }
    
    public ImageAccess getImageAccess() {
        int pageSize = Integer.valueOf(config.getProperty("image.page.size"));
        return new ImageAccessImplMongoDB(mongoClient, DBNAME, objectMapper, pageSize);
    }
    
    public TemplateAccess getTemplateAccess() {
        int pageSize = Integer.valueOf(config.getProperty("template.page.size"));
        return new TemplateAccessImplMongoDB(mongoClient, DBNAME, objectMapper, pageSize);
    }
    
    public ImageMetricsService getImageMetricsService() {
        int pageSize = Integer.valueOf(config.getProperty("imagemetricsservice.page.size"));
        return new ImageMetricsServiceImplMongoDB(mongoClient, DBNAME, objectMapper, pageSize);
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
        if (!collNames.contains("PageCursor")) {
            initPageCursor(images3DB);
        }
        if (!collNames.contains("ImagePlant")) {
            initImagePlant(images3DB);
        }
        if (!collNames.contains("Template")) {
            initTemplate(images3DB);
        }
        if (!collNames.contains("Image")) {
            initImage(images3DB);
        }
    }
    
    private void initPageCursor(DB db) {
        DBCollection coll = db.getCollection("PageCursor");
        coll.createIndex(new BasicDBObject("id", "hashed"));
        coll.createIndex(new BasicDBObject("creationTime", 1));
    }
    
    private void initImagePlant(DB db) {
        DBCollection coll = db.getCollection("ImagePlant");
        coll.createIndex(new BasicDBObject("id", "hashed"));
        coll.createIndex(new BasicDBObject("name", 1), new BasicDBObject("unique", true));
        coll.createIndex(new BasicDBObject("creationTime", 1));
    }
    
    private void initTemplate(DB db) {
        DBCollection coll = db.getCollection("Template");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("name", 1), 
                new BasicDBObject("unique", true));
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("isArchived", 1));
    }
    
    private void initImage(DB db) {
        DBCollection coll = db.getCollection("Image");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("id", 1), 
                new BasicDBObject("unique", true));
        coll.createIndex(
                new BasicDBObject()
                    .append("imagePlantId", 1)
                    .append("version.templateName", 1)
                    .append("version.originalImageId", 1));
    }
    
}
