package com.images3.core.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.images3.ImageIdentity;
import com.images3.VersionIdentity;
import com.images3.core.infrastructure.spi.VersionAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class VersionAccessImplMongoDB extends MongoDBAccess implements VersionAccess {

    public VersionAccessImplMongoDB(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper, int pageSize) {
        super(mongoClient, dbname, objectMapper, pageSize);
    }

    public void insertVersion(VersionOS version) {
        DBCollection coll = getDatabase().getCollection("Version");
        coll.insert(getObjectMapper().mapToBasicDBObject(version));
    }

    public void deleteVersionsByImageId(ImageIdentity imageId) {
        DBCollection coll = getDatabase().getCollection("Version");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imageId.getImagePlantId())
                                    .append("imageId", imageId.getImageId());
        coll.remove(criteria);
    }

    public VersionOS selectVersionById(VersionIdentity id) {
        DBCollection coll = getDatabase().getCollection("Version");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", id.getImageId().getImagePlantId())
                                    .append("imageId", id.getImageId().getImageId())
                                    .append("templateId", id.getTemplateId());
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToVersionOS((BasicDBObject) cursor.next());
    }

    public PaginatedResult<List<VersionOS>> selectVersionsByImageId(ImageIdentity imageId) {
        return new PaginatedResult<List<VersionOS>>(
                this, "getVersionsByImageId", new Object[]{imageId}) {};
    }
    
    public List<VersionOS> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getVersionsByImageId".equals(methodName)) {
            ImageIdentity imageId = (ImageIdentity) arguments[0];
            return getVersionsByImageId(imageId, (PageCursor) pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }

    private List<VersionOS> getVersionsByImageId(ImageIdentity imageId, PageCursor pageCursor) {
        if (null == pageCursor) {
            pageCursor = new PageCursor().startAtPage(1).withAll();
        }
        DBCollection coll = getDatabase().getCollection("Version");
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        List<DBObject> objects = coll.find().skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<VersionOS> versions = new ArrayList<VersionOS>(objects.size());
        for (DBObject obj: objects) {
            versions.add(getObjectMapper().mapToVersionOS((BasicDBObject) obj));
        }
        return versions;
    }
    
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<VersionOS> result) {
        if (null != result 
                && result.size() == 0) {
            return null;
        }
        return getNextPageCursor((String) pageCursor)[0];
    }

}
