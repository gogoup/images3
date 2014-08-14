package com.images3.core.infrastructure;

import com.images3.utility.PageCursor;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public abstract class MongoDBAccess {

    private MongoClient mongoClient;
    private String dbname;
    private MongoDBObjectMapper objectMapper;
    
    public MongoDBAccess(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper) {
        this.mongoClient = mongoClient;
        this.dbname = dbname;
        this.objectMapper = objectMapper;
    }
    
    protected DB getDatabase() {
        return mongoClient.getDB(dbname);
    }
    
    protected MongoDBObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    protected void checkForAffectedDocuments(WriteResult result, int expectedAffectedNumber) {
        if (result.getN() != expectedAffectedNumber) {
            throw new RuntimeException(
                    "Unexpected affected number of documents,"
                    + "" + result.getN() + " != " +expectedAffectedNumber);
        }
    }
    
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor) {
        PageCursor cursor = (PageCursor) pageCursor;
        if (null == cursor) {
            return new PageCursor().startAtPage(1).withSize(10);
        } else {
            return new PageCursor().startAtPage(cursor.getStart() + 1).withSize(cursor.getSize());
        }
    }
    
    protected void checkForQueryTooManyRecords(Object pageCursor) {
        if (null == pageCursor) {
            throw new UnsupportedOperationException("Too many records to find.");
        }
    }
    
}
