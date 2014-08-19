package com.images3.core.infrastructure;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public abstract class MongoDBAccess {

    private MongoClient mongoClient;
    private String dbname;
    private MongoDBObjectMapper objectMapper;
    private int pageSize;
    
    public MongoDBAccess(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper, int pageSize) {
        this.mongoClient = mongoClient;
        this.dbname = dbname;
        this.objectMapper = objectMapper;
        this.pageSize = pageSize;
    }
    
    protected DB getDatabase() {
        return mongoClient.getDB(dbname);
    }
    
    protected MongoDBObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    protected int getPageSize() {
        return pageSize;
    }
    
    protected void checkForAffectedDocuments(WriteResult result, int expectedAffectedNumber) {
        if (result.getN() != expectedAffectedNumber) {
            throw new RuntimeException(
                    "Unexpected affected number of documents,"
                    + "" + result.getN() + " != " +expectedAffectedNumber);
        }
    }
    
    protected Object[] getNextPageCursor(String pageCursor) {
        PageCursor cursor = null;
        String nextPageCursor = ShortUUID.randomUUID();
        if (null != pageCursor) {
            cursor = selectPageCursorById(pageCursor);
        }
        cursor = getNextPageCursor(cursor);
        insertPageCursor(nextPageCursor, cursor);
        return new Object[] {nextPageCursor, cursor};
    }
    
    private PageCursor getNextPageCursor(PageCursor cursor) {
        if (null == cursor) {
            return new PageCursor().startAtPage(1).withSize(pageSize);
        } else {
            return new PageCursor().startAtPage(cursor.getStart() + 1).withSize(pageSize);
        }
    }
    
    protected void checkForQueryTooManyRecords(Object pageCursor) {
        if (null == pageCursor) {
            throw new UnsupportedOperationException("Too many records to find.");
        }
    }
    
    private void insertPageCursor(String id, PageCursor pageCursor) {
        DBCollection coll = getDatabase().getCollection("PageCursor");
        coll.insert(getObjectMapper().mapToBasicDBObject(id, pageCursor));
    }
    
    private PageCursor selectPageCursorById(String id) {
        DBCollection coll = getDatabase().getCollection("PageCursor");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("id", id);
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToPageCursor((BasicDBObject) cursor.next());
    }
    
}
