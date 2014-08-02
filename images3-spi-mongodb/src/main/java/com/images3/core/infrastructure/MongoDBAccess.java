package com.images3.core.infrastructure;

import com.images3.utility.PageCursor;
import com.mongodb.DB;
import com.mongodb.WriteResult;

public abstract class MongoDBAccess {

    private MongoClientAdmin clientAdmin;
    private MongoDBObjectMapper objectMapper;
    
    public MongoDBAccess(MongoClientAdmin clientAdmin, MongoDBObjectMapper objectMapper) {
        this.clientAdmin = clientAdmin;
        this.objectMapper = objectMapper;
    }
    
    protected DB getDatabase() {
        return clientAdmin.getDatabase();
    }
    
    protected MongoDBObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    protected void checkForAffectedDocuments(WriteResult result, int expectedAffectedNumber) {
        if (result.getN() != expectedAffectedNumber) {
            throw new RuntimeException(
                    "Unexpected affected number of documents," + result.getN() + " != " +expectedAffectedNumber);
        }
    }
    
    public Object getNextPageCursor(String methodName, Object[] arguments,
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
