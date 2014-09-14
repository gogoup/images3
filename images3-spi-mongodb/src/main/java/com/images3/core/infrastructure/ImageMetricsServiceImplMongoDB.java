package com.images3.core.infrastructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.NoSuchEntityFoundException;
import com.images3.common.TemplateIdentity;
import com.images3.common.TimeInterval;
import com.images3.core.infrastructure.spi.ImageMetricsService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ImageMetricsServiceImplMongoDB extends MongoDBAccess<ImageMetricsOS> implements ImageMetricsService {

    public ImageMetricsServiceImplMongoDB(MongoClient mongoClient,
            String dbname, MongoDBObjectMapper objectMapper, int pageSize) {
        super(mongoClient, dbname, objectMapper, pageSize);
    }

    @Override
    public void record(ImageOS image) {
        ImageMetricsOS metrics = createMetrics(image);
        updateSecondMetrics(metrics);
        updateTemplateMetrics(metrics);
    }
    
    private void updateSecondMetrics(ImageMetricsOS metrics) {
        DBCollection coll = getDatabase().getCollection("ImageMetrics");
        BasicDBObject criteria = new BasicDBObject()
                .append("imagePlantId", metrics.getImagePlantId())
                .append("templateName", metrics.getTemplateName())
                .append("second", metrics.getSecond());
        BasicDBObject returnFields = new BasicDBObject();
        BasicDBObject sort = new BasicDBObject();
        boolean remove = false;
        BasicDBObject increase = new BasicDBObject()
            .append("numberOfImages", metrics.getNumberOfImages())
            .append("sizeOfImages", metrics.getSizeOfImages());
        BasicDBObject update = new BasicDBObject()
                .append("$inc", increase);
        boolean returnNew = true;
        boolean upsert = true;
        coll.findAndModify(criteria, returnFields, sort, remove, update, returnNew, upsert);
    }
    
    private void updateTemplateMetrics(ImageMetricsOS metrics) {
        DBCollection coll = getDatabase().getCollection("ImageMetrics");
        BasicDBObject criteria = new BasicDBObject()
                .append("imagePlantId", metrics.getImagePlantId())
                .append("templateName", metrics.getTemplateName())
                .append("second", 0);
        BasicDBObject returnFields = new BasicDBObject();
        BasicDBObject sort = new BasicDBObject();
        boolean remove = false;
        BasicDBObject increase = new BasicDBObject()
            .append("numberOfImages", metrics.getNumberOfImages())
            .append("sizeOfImages", metrics.getSizeOfImages());
        BasicDBObject update = new BasicDBObject()
            .append("$inc", increase);
        boolean returnNew = true;
        boolean upsert = true;
        coll.findAndModify(criteria, returnFields, sort, remove, update, returnNew, upsert);
    }

    @Override
    public long calculateNumberOfImages(String imagePlantId) {
        long counts = 0;
        List<DBObject> objects = selectMetricsByImagePlantId(imagePlantId);
        for (DBObject obj: objects) {
            BasicDBObject object = (BasicDBObject) obj;
            counts += object.getLong("numberOfImages");
        }
        return counts;
    }
    
    @Override
    public long calculateNumberOfImages(TemplateIdentity templateId) {
        BasicDBObject object = (BasicDBObject) selectMetricsByTempalteId(templateId);
        return object.getLong("numberOfImages");
    }
    
    @Override
    public long calculateSizeOfImages(String imagePlantId) {
        long size = 0;
        List<DBObject> objects = selectMetricsByImagePlantId(imagePlantId);
        for (DBObject obj: objects) {
            BasicDBObject object = (BasicDBObject) obj;
            size += object.getLong("sizeOfImages");
        }
        return size;
    }

    @Override
    public long calculateSizeOfImages(TemplateIdentity templateId) {
        BasicDBObject object = (BasicDBObject) selectMetricsByTempalteId(templateId);
        return object.getLong("sizeOfImages");
    }

    private List<DBObject> selectMetricsByImagePlantId(String imagePlantId) {
        DBCollection coll = getDatabase().getCollection("ImageMetrics");
        BasicDBObject criteria = new BasicDBObject()
                                        .append("imagePlantId", imagePlantId)
                                        .append("second", 0);
        return coll.find(criteria).toArray();
    }

    private DBObject selectMetricsByTempalteId(TemplateIdentity templateId) {
        DBCollection coll = getDatabase().getCollection("ImageMetrics");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", templateId.getImagePlantId())
                                    .append("templateName", templateId.getTemplateName())
                                    .append("second", 0);
        DBObject object = coll.findOne(criteria);
        if (null == object) {
            throw new NoSuchEntityFoundException("NumberOfImages", templateId.toString());
        }
        return object;
    }

    @Override
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            String imagePlantId, TimeInterval interval) {
        return new PaginatedResult<List<ImageMetricsOS>>(
                this, "retrieveStatsByImagePlantId", new Object[] {imagePlantId, interval});
    }

    @Override
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            TemplateIdentity templateId, TimeInterval interval) {
        return new PaginatedResult<List<ImageMetricsOS>>(
                this, "retrieveStatsByTemplateId", new Object[] {templateId, interval});
    }
    
    private ImageMetricsOS createMetrics(ImageOS image) {
        long second = getSecond(image.getDateTime());
        return new ImageMetricsOS(
                image.getId().getImagePlantId(), 
                image.getVersion().getTemplateName(),
                second,
                1, 
                image.getMetadata().getSize());
    }

    private long getSecond(Date dateTime) {
        long time = dateTime.getTime();
        long restOfSecond = time % 1000;
        long second = time - (restOfSecond);
        second /= 1000;
        return second;
    }

    @Override
    public List<ImageMetricsOS> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("retrieveStatsByImagePlantId".equals(tag)) {
            String imagePlantId = (String) arguments[0];
            TimeInterval interval = (TimeInterval) arguments[1];
            Object[] pageResult = retrieveNextPageCursor((String) pageCursor);
            PageCursor cursor = (PageCursor) pageResult[1];
            return getStatsByImagePlantId(imagePlantId, interval, cursor);
        }
        if ("retrieveStatsByTemplateId".equals(tag)) {
            TemplateIdentity templateIdentity = (TemplateIdentity) arguments[0];
            TimeInterval interval = (TimeInterval) arguments[1];
            Object[] pageResult = retrieveNextPageCursor((String) pageCursor);
            PageCursor cursor = (PageCursor) pageResult[1];
            return getStatsByTemplateId(templateIdentity, interval, cursor);
        }
        throw new UnsupportedOperationException(tag);
    }

    @Override
    public boolean isFetchAllResultsSupported(String tag, Object[] arguments) {
        return false;
    }

    @Override
    public List<ImageMetricsOS> fetchAllResults(String tag, Object[] arguments) {
        throw new UnsupportedOperationException(tag);
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<ImageMetricsOS> result) {
        return nextPageCursor(tag, arguments, pageCursor, result);
    }
    
    private List<ImageMetricsOS> getStatsByImagePlantId(String imagePlantId,
            TimeInterval interval, PageCursor cursor) {
        long[] timeBounds = getTimeBounds(interval);
        BasicDBObject secondRange = new BasicDBObject()
                                    .append("$gte", timeBounds[0])
                                    .append("$lte", timeBounds[1]);
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("second", secondRange);
        return getImageStats(criteria, cursor);
    }
    
    private List<ImageMetricsOS> getStatsByTemplateId(TemplateIdentity templateId,
            TimeInterval interval, PageCursor cursor) {
        long[] timeBounds = getTimeBounds(interval);
        BasicDBObject secondRange = new BasicDBObject()
                                    .append("$gte", timeBounds[0])
                                    .append("$lte", timeBounds[1]);
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", templateId.getImagePlantId())
                                    .append("templateName", templateId.getTemplateName())
                                    .append("second", secondRange);
        return getImageStats(criteria, cursor);
    }
    
    private long[] getTimeBounds(TimeInterval interval) {
        long startSecond = getSecond(interval.getStart());
        long endSecond = getSecond(interval.getEnd());
        return new long[] {startSecond, endSecond};
    }
    
    private List<ImageMetricsOS> getImageStats(BasicDBObject criteria, PageCursor pageCursor) {
        DBCollection coll = getDatabase().getCollection("ImageMetrics");
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        List<DBObject> objects = coll.find(criteria).skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<ImageMetricsOS> metrics = new ArrayList<ImageMetricsOS>(objects.size());
        for (DBObject obj: objects) {
            metrics.add(getObjectMapper().mapToImageMetricsOS((BasicDBObject) obj));
        }
        return metrics;
    }

    @Override
    public Object getFirstPageCursor(String tag, Object[] arguments) {
        return retrieveNextPageCursor(null)[0];
    }

}
