package com.images3.core.infrastructure;

import java.util.Date;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public abstract class MongoDBAccess<T> {

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
    
    protected Object nextPageCursorId(String tag, Object[] arguments,
            Object pageCursor, List<T> result) {
        if (foundTheLastRecord(result)) {
            return PaginatedResult.NONE_PAGE_CURSOR;
        }
        return nextPageCursor((String) pageCursor).getId();
    }

    protected PageCursor nextPageCursor(String pageCursorId) {
        PageCursor cursor = null;
        if (null != pageCursorId) {
            cursor = selectPageCursorById(pageCursorId);
        }
        Page page = createNextPage(cursor.getPage());
        PageCursor nextPageCursor = createPageCursor(page, cursor);
        insertPageCursor(nextPageCursor);
        return nextPageCursor;
    }
    
    private Page createNextPage(Page page) {
        if (null == page) {
            return createFirstPage();
        } else {
            return new Page().startAtPage(page.getStart() + 1).withSize(pageSize);
        }
    }
    
    private Page createFirstPage() {
        return new Page().startAtPage(1).withSize(pageSize); //first page
    }
    
    private PageCursor createPageCursor(Page page, PageCursor prevPageCursor) {
        String id = ShortUUID.randomUUID();
        Date createdTime = new Date(System.currentTimeMillis());
        return new PageCursor(
                id, 
                (prevPageCursor == null ? null : prevPageCursor.getId()), 
                page, 
                createdTime);
    }
    
    private void insertPageCursor(PageCursor cursor) {
        DBCollection coll = getDatabase().getCollection("PageCursor");
        coll.insert(getObjectMapper().mapToBasicDBObject(cursor));
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
    
    private boolean foundTheLastRecord(List<T> result) {
        return (null != result 
                && (result.size() == 0
                    || getPageSize() > result.size()));
    }
}
