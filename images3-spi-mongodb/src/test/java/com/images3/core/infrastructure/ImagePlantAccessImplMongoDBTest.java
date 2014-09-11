package com.images3.core.infrastructure;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import com.images3.common.AmazonS3Bucket;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class ImagePlantAccessImplMongoDBTest {
    
    private static MongoDBAccessProvider provider;

    public static void main(String[] args) {
        //String pathToConfig = ImagePlantAccessImplMongoDBTest.class.getResource("/config.properties").getPath();
        //provider = new MongoDBAccessProvider(pathToConfig);
        testMetrics();
        /*
        ImagePlantAccess imagePlantAccess = provider.getImagePlantAccess();
        String id  = imagePlantAccess.genertateImagePlantId();
        System.out.println("ID: " + id);
        if (!imagePlantAccess.isDuplicatedImagePlantName("TestImagePlant")) {
            imagePlantAccess.insertImagePlant(new ImagePlantOS(id, "TestImagePlant", new Date(),
                    new AmazonS3Bucket("AccessKey123", "SecretKey123" , "TestAccessKey"), "MasterTemplateName"));
        }
        */
        //ImagePlantOS imagePlant = imagePlantAccess.selectImagePlantById("ID123");
    }
    
    public static void testMetrics() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(new ServerAddress("192.168.59.103", 27017));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } 
        DB images3DB = mongoClient.getDB("images3");
        DBCollection coll = images3DB.getCollection("ImageMetricsInTemplate");
        BasicDBObject criteria = new BasicDBObject()
                .append("imagePlantId", "ImagePlantId_222")
                .append("templateName", "Original");
        BasicDBObject returnFields = new BasicDBObject();
        BasicDBObject sort = new BasicDBObject();
        boolean remove = false;
        BasicDBObject increase = new BasicDBObject()
            .append("numberOfImages", 1)
            .append("sizeOfImages", 1000);
        BasicDBObject update = new BasicDBObject()
                .append("$inc", increase);
        boolean returnNew = true;
        boolean upsert = true;
        coll.findAndModify(criteria, returnFields, sort, remove, update, returnNew, upsert);
    }
}
