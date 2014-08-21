package com.images3.core.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.images3.ImageIdentity;
import com.images3.core.infrastructure.spi.ImageAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class ImageAccessImplMongoDB extends MongoDBAccess<ImageOS> implements ImageAccess {

    public ImageAccessImplMongoDB(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper, int pageSize) {
        super(mongoClient, dbname, objectMapper, pageSize);
    }

    @Override
    public boolean isDuplicateVersion(String imagePlantId, VersionOS version) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("version.templateName", version.getTemplateName())
                                    .append("version.originalImageId", version.getOriginalImageId());
        DBCursor cursor = coll.find(criteria);
        return cursor.hasNext();
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

    @Override
    public ImageOS selectImageByVersion(String imagePlantId, VersionOS version) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("version.templateName", version.getTemplateName())
                                    .append("version.originalImageId", version.getOriginalImageId());
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToImageOS((BasicDBObject) cursor.next());
    }

    @Override
    public PaginatedResult<List<ImageOS>> selectImagesByOriginalImageId(
            String imagePlantId, String originalImageId) {
        return new PaginatedResult<List<ImageOS>>(
                this, "getImagesByOriginalImageId", new Object[] {imagePlantId, originalImageId}) {};
    }

    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(
            String imagePlantId) {
        return new PaginatedResult<List<ImageOS>>(
                this, "getImagesByImagePlantId", new Object[] {imagePlantId}) {};
    }
    
    public List<ImageOS> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getImagesByOriginalImageId".equals(methodName)) {
            String imagePlantId = (String) arguments[0];
            String originalImageId = (String) arguments[1];
            Object[] pageResult = getNextPageCursor((String) pageCursor);
            PageCursor cursor = (PageCursor) pageResult[1];
            return getImagesByOriginalImageId(imagePlantId, originalImageId, cursor);
        }
        if ("getImagesByImagePlantId".equals(methodName)) {
            String imagePlantId = (String) arguments[0];
            Object[] pageResult = getNextPageCursor((String) pageCursor);
            PageCursor cursor = (PageCursor) pageResult[1];
            return getImagesByImagePlantId(imagePlantId, cursor);
        }
        throw new UnsupportedOperationException(methodName);
    }
    
    private List<ImageOS> getImagesByOriginalImageId(String imagePlantId, 
            String originalImageId, PageCursor pageCursor) {
        checkForQueryTooManyRecords(pageCursor);
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("version.originalImageId", originalImageId);
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        List<DBObject> objects = coll.find(criteria).skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<ImageOS> images = new ArrayList<ImageOS>(objects.size());
        for (DBObject obj: objects) {
            images.add(getObjectMapper().mapToImageOS((BasicDBObject) obj));
        }
        return images;
    }

    private List<ImageOS> getImagesByImagePlantId(String imagePlantId, 
            PageCursor pageCursor) {
        checkForQueryTooManyRecords(pageCursor);
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId);
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        List<DBObject> objects = coll.find(criteria).skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<ImageOS> images = new ArrayList<ImageOS>(objects.size());
        for (DBObject obj: objects) {
            images.add(getObjectMapper().mapToImageOS((BasicDBObject) obj));
        }
        return images;
    }
    
}
