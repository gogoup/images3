package com.images3.core.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.images3.core.infrastructure.spi.ImagePlantAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class ImagePlantAccessImplMongoDB extends MongoDBAccess<ImagePlantOS> implements ImagePlantAccess {
    
    public ImagePlantAccessImplMongoDB(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper, int pageSize) {
        super(mongoClient, dbname, objectMapper, pageSize);
    }

    public boolean isDuplicatedImagePlantName(String name) {
        DBCollection coll = getDatabase().getCollection("ImagePlant");
        BasicDBObject criteria = new BasicDBObject().append("name", name);
        DBCursor cursor = coll.find(criteria);
        return cursor.hasNext();
    }

    public String genertateImagePlantId() {
        return ShortUUID.randomUUID();
    }

    public void insertImagePlant(ImagePlantOS imagePlant) {
        DBCollection coll = getDatabase().getCollection("ImagePlant");
        coll.insert(getObjectMapper().mapToBasicDBObject(imagePlant));
    }

    public void updateImagePlant(ImagePlantOS imagePlant) {
        DBCollection coll = getDatabase().getCollection("ImagePlant");
        BasicDBObject criteria = new BasicDBObject().append("id", imagePlant.getId());
        WriteResult result = coll.update(criteria, getObjectMapper().mapToBasicDBObject(imagePlant));
        checkForAffectedDocuments(result, 1);
    }

    public void deleteImagePlant(ImagePlantOS imagePlant) {
        DBCollection coll = getDatabase().getCollection("ImagePlant");
        BasicDBObject criteria = new BasicDBObject().append("id", imagePlant.getId());
        WriteResult result = coll.remove(criteria);
        checkForAffectedDocuments(result, 1);
    }

    public ImagePlantOS selectImagePlantById(String id) {
        DBCollection coll = getDatabase().getCollection("ImagePlant");
        BasicDBObject criteria = new BasicDBObject().append("id", id);
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToImagePlantOS((BasicDBObject) cursor.next());
    }

    public PaginatedResult<List<ImagePlantOS>> selectAllImagePlants() {
        return new PaginatedResult<List<ImagePlantOS>>(this, "getAllImagePlants", new Object[0]) {};
    }
    
    public List<ImagePlantOS> fetchResult(String tag,
            Object[] arguments, Object pageCursor) {
        if ("getAllImagePlants".equals(tag)) {
            PageCursor cursor = selectPageCursorById((String) pageCursor);
            return getAllImagePlants(cursor.getPage());
        }
        throw new UnsupportedOperationException(tag);
    }

    @Override
    public boolean isFetchAllResultsSupported(String tag, Object[] arguments) {
        if ("getAllImagePlants".equals(tag)) {
            return false;
        }
        throw new UnsupportedOperationException(tag);
    }

    @Override
    public List<ImagePlantOS> fetchAllResults(String tag, Object[] arguments) {
        throw new UnsupportedOperationException(tag);
    }

    private List<ImagePlantOS> getAllImagePlants(Page pageCursor) {
        DBCollection coll = getDatabase().getCollection("ImagePlant");
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        List<DBObject> objects = coll.find().skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<ImagePlantOS> imagePlants = new ArrayList<ImagePlantOS>(objects.size());
        for (DBObject obj: objects) {
            imagePlants.add(getObjectMapper().mapToImagePlantOS((BasicDBObject) obj));
        }
        return imagePlants;
    }
    
    @Override
    public Object getFirstPageCursor(String tag, Object[] arguments) {
        return nextPageCursor(null).getId();
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<ImagePlantOS> result) {
        return nextPageCursorId(tag, arguments, pageCursor, result);
    }

    @Override
    public Object getPrevPageCursor(String tag, Object[] arguments, Object pageCursor,
            List<ImagePlantOS> result) {
        return previousPageCursorId(tag, arguments, pageCursor, result);
    }

}
