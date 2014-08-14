package com.images3.core.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.images3.ImageIdentity;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.utility.PageCursor;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class ImageAccessImplMongoDB extends MongoDBAccess implements ImageAccess {

    public ImageAccessImplMongoDB(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper) {
        super(mongoClient, dbname, objectMapper);
    }

    public String generateImageId(ImagePlantOS imagePlant) {
        return ShortUUID.randomUUID();
    }

    public void insertImage(ImageOS image) {
        DBCollection coll = getDatabase().getCollection("Image");
        coll.insert(getObjectMapper().mapToBasicDBObject(image));
    }

    public void deleteImage(ImageOS image) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", image.getId().getImagePlantId())
                                    .append("id", image.getId().getImageId());
        WriteResult result = coll.remove(criteria);
        checkForAffectedDocuments(result, 1);
    }

    public ImageOS selectImageById(ImageIdentity id) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", id.getImagePlantId())
                                    .append("id", id.getImageId());
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToImageOS((BasicDBObject) cursor.next());
    }

    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(
            String imagePlantId) {
        return new PaginatedResult<List<ImageOS>>(
                this, "getImagesByImagePlantId", new Object[] {imagePlantId}) {};
    }
    
    public List<ImageOS> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getImagesByImagePlantId".equals(methodName)) {
            String imagePlantId = (String) arguments[0];
            return getImagesByImagePlantId(imagePlantId, (PageCursor) pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }

    private List<ImageOS> getImagesByImagePlantId(String imagePlantId, 
            PageCursor pageCursor) {
        checkForQueryTooManyRecords(pageCursor);
        DBCollection coll = getDatabase().getCollection("Image");
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        List<DBObject> objects = coll.find().skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<ImageOS> images = new ArrayList<ImageOS>(objects.size());
        for (DBObject obj: objects) {
            images.add(getObjectMapper().mapToImageOS((BasicDBObject) obj));
        }
        return images;
    }

}
