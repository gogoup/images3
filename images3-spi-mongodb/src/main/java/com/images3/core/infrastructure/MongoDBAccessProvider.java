package com.images3.core.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
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
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBAccessProvider {
    
    private static final String DBNAME = "images3";
    
    private Properties config;
    private MongoClient mongoClient;
    private MongoDBObjectMapper objectMapper;
    
    public MongoDBAccessProvider(String pathToConfig) {
        this(readConfigProperties(pathToConfig));
    }
    
    public MongoDBAccessProvider(Properties config) {
        this.config = config;
        initMongoClient();
        initCollections();
        objectMapper = new MongoDBObjectMapper();
    }
    
    private static Properties readConfigProperties(String pathToConfig) {
        Properties config = new Properties();
        try {
            InputStream in = new FileInputStream(new File(pathToConfig));
            config.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
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
        String username = config.getProperty("mongodb.username");
        String password = config.getProperty("mongodb.password");
        try {
            if (username.trim().length() == 0) {
                this.mongoClient = new MongoClient(new ServerAddress(url, port));
            } else {
                MongoCredential credential = 
                        MongoCredential.createMongoCRCredential(username, DBNAME, password.toCharArray());
                this.mongoClient = new MongoClient(new ServerAddress(url, port), Arrays.asList(credential));
            }
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
        if (!collNames.contains("ImageMetrics")) {
            initImageMetrics(images3DB);
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
        coll.createIndex(new BasicDBObject("nameKey", 1), new BasicDBObject("unique", true));
        coll.createIndex(new BasicDBObject("creationTime", 1));
    }
    
    private void initTemplate(DB db) {
        DBCollection coll = db.getCollection("Template");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1).append("nameKey", 1), 
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
    
    private void initImageMetrics(DB db) {
        DBCollection coll = db.getCollection("ImageMetrics");
        coll.createIndex(
                new BasicDBObject().append("imagePlantId", 1)
                    .append("templateName", 1)
                    .append("second", 1), 
                new BasicDBObject("unique", true));
    }
    
}
