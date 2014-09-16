package com.images3.core.infrastructure;

import java.util.ArrayList;
import java.util.List;

import com.images3.common.TemplateIdentity;
import com.images3.core.infrastructure.spi.TemplateAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class TemplateAccessImplMongoDB extends MongoDBAccess<TemplateOS> implements TemplateAccess {

    public TemplateAccessImplMongoDB(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper, int pageSize) {
        super(mongoClient, dbname, objectMapper, pageSize);
    }

    public boolean isDuplicatedTemplateName(String imagePlantId, String name) {
        DBCollection coll = getDatabase().getCollection("Template");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("name", name);
        DBCursor cursor = coll.find(criteria);
        return cursor.hasNext();
    }

    public void insertTemplate(TemplateOS template) {
        DBCollection coll = getDatabase().getCollection("Template");
        coll.insert(getObjectMapper().mapToBasicDBObject(template));
    }

    public void updateTemplate(TemplateOS template) {
        DBCollection coll = getDatabase().getCollection("Template");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", template.getId().getImagePlantId())
                                    .append("name", template.getId().getTemplateName());
        WriteResult result = coll.update(criteria, getObjectMapper().mapToBasicDBObject(template));
        checkForAffectedDocuments(result, 1);
    }

    public void deleteTemplate(TemplateOS template) {
        DBCollection coll = getDatabase().getCollection("Template");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", template.getId().getImagePlantId())
                                    .append("name", template.getId().getTemplateName());
        WriteResult result = coll.remove(criteria);
        checkForAffectedDocuments(result, 1);
    }

    public void deleteTemplatesByImagePlantId(String imagePlantId) {
        DBCollection coll = getDatabase().getCollection("Template");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId);
        coll.remove(criteria);
    }

    public TemplateOS selectTemplateById(TemplateIdentity id) {
        DBCollection coll = getDatabase().getCollection("Template");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", id.getImagePlantId())
                                    .append("name", id.getTemplateName());
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToTemplateOS((BasicDBObject) cursor.next());
    }

    public PaginatedResult<List<TemplateOS>> selectTemplatesByImagePlantId(
            String imagePlantId, Boolean isArchived) {
        return new PaginatedResult<List<TemplateOS>>(
                this, "getTemplatesByImagePlantId", new Object[]{imagePlantId, isArchived});
    }
    
    public List<TemplateOS> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getTemplatesByImagePlantId".equals(tag)) {
            String imagePlantId = (String) arguments[0];
            Boolean isArchived = (Boolean) arguments[1];
            PageCursor cursor = selectPageCursorById((String) pageCursor);
            return getTemplatesByImagePlantId(imagePlantId, isArchived, cursor.getPage());
        }
        throw new UnsupportedOperationException(tag);
    }

    @Override
    public boolean isFetchAllResultsSupported(String tag, Object[] arguments) {
        if ("getTemplatesByImagePlantId".equals(tag)) {
            return true;
        }
        throw new UnsupportedOperationException(tag);
    }

    @Override
    public List<TemplateOS> fetchAllResults(String tag, Object[] arguments) {
        if ("getTemplatesByImagePlantId".equals(tag)) {
            String imagePlantId = (String) arguments[0];
            Boolean isArchived = (Boolean) arguments[1];
            return getTemplatesByImagePlantId(imagePlantId, isArchived);
        }
        throw new UnsupportedOperationException(tag);
    }

    private List<TemplateOS> getTemplatesByImagePlantId(String imagePlantId, 
            Boolean isArchived, Page pageCursor) {
        DBCollection coll = getDatabase().getCollection("Template");
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        BasicDBObject criteria = new BasicDBObject()
                                        .append("imagePlantId", imagePlantId);
        if (null != isArchived) {
            criteria.append("isArchived", isArchived);  
        }
        List<DBObject> objects = coll.find(criteria).skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<TemplateOS> templates = new ArrayList<TemplateOS>(objects.size());
        for (DBObject obj: objects) {
            templates.add(getObjectMapper().mapToTemplateOS((BasicDBObject) obj));
        }
        return templates;
    }
    
    private List<TemplateOS> getTemplatesByImagePlantId(String imagePlantId, 
            Boolean isArchived) {
        DBCollection coll = getDatabase().getCollection("Template");
        BasicDBObject criteria = new BasicDBObject()
                                        .append("imagePlantId", imagePlantId);
        if (null != isArchived) {
            criteria.append("isArchived", isArchived);  
        }
        List<DBObject> objects = coll.find(criteria).toArray();
        List<TemplateOS> templates = new ArrayList<TemplateOS>(objects.size());
        for (DBObject obj: objects) {
            templates.add(getObjectMapper().mapToTemplateOS((BasicDBObject) obj));
        }
        return templates;
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<TemplateOS> result) {
        return nextPageCursorId(tag, arguments, pageCursor, result);
    }

    @Override
    public Object getFirstPageCursor(String tag, Object[] arguments) {
        return nextPageCursor(null).getId();
    }

    @Override
    public Object getPrevPageCursor(String tag, Object[] arguments, Object pageCursor,
            List<TemplateOS> result) {
        return previousPageCursorId(tag, arguments, pageCursor, result);
    }

}
