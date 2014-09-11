package com.images3.core.infrastructure;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.TemplateIdentity;
import com.images3.common.TimeInterval;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class ImagePlantAccessImplMongoDBTest {
    
    private static MongoDBAccessProvider provider;

    public static void main(String[] args) {
        //String pathToConfig = ImagePlantAccessImplMongoDBTest.class.getResource("/config.properties").getPath();
        //provider = new MongoDBAccessProvider(pathToConfig);
        //testMetrics();
        getStatsByImagePlantId();
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
        DBCollection coll = images3DB.getCollection("ImageMetrics");
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
    
    public static void getStatsByImagePlantId() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(new ServerAddress("192.168.59.103", 27017));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } 
        DB images3DB = mongoClient.getDB("images3");
        DBCollection coll = images3DB.getCollection("ImageMetrics");
        
        String imagePlantId = "jrlffoteqoinnpepur68mw";
        long[] timeBounds = new long[] {1410482109, 1410483734};
        BasicDBObject secondRange = new BasicDBObject()
                                        .append("$gte", timeBounds[0])
                                        .append("$lte", timeBounds[1]);
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("second", secondRange);
        List<DBObject> objects = coll.find(criteria).toArray();
        int summary = 0;
        for (DBObject obj: objects) {
            BasicDBObject object = (BasicDBObject) obj;
            summary += object.getLong("numberOfImages");
        }
        System.out.println(summary);
    }
    
    public static void getStatsByTemplateId() {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(new ServerAddress("192.168.59.103", 27017));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } 
        DB images3DB = mongoClient.getDB("images3");
        DBCollection coll = images3DB.getCollection("ImageMetrics");
        String imagePlantId = "jrlffoteqoinnpepur68mw";
        String templateName = "Master";
        long[] timeBounds = new long[] {1410482109, 1410482110};
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("templateName", templateName)
                                    .append("$gte", timeBounds[0])
                                    .append("$lte", timeBounds[1]);
        List<DBObject> objects = coll.find(criteria).toArray();
        System.out.println("HERE======>" + objects);
    }
    
    private static long getSecond(Date dateTime) {
        long time = dateTime.getTime();
        long restOfSecond = time % 1000;
        long second = time - (restOfSecond);
        second /= 1000;
        return second;
    }
    
    private static long[] getTimeBounds(TimeInterval interval) {
        long startSecond = getSecond(interval.getStart());
        long endSecond = startSecond;
        if (interval.getUnit() == TimeUnit.SECONDS) {
            endSecond += interval.getLength();
        } else if (interval.getUnit() == TimeUnit.MINUTES) {
            endSecond += (interval.getLength() * 60);
        } else if (interval.getUnit() == TimeUnit.HOURS) {
            endSecond += (interval.getLength() * 60 * 60);
        } else if (interval.getUnit() == TimeUnit.DAYS) {
            endSecond += (interval.getLength() * 60 * 60 * 24);
        }
        return new long[] {startSecond * 1000, endSecond * 1000};
    }
    
    
}
