package com.images3.core.infrastructure;

import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.TemplateIdentity;
import com.images3.common.TimeInterval;
import com.images3.core.infrastructure.spi.ImageMetricsService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
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
        DBCollection coll = getDatabase().getCollection("ImageMetricsInSecond");
        BasicDBObject criteria = new BasicDBObject()
                .append("imagePlantId", metrics.getImagePlantId())
                .append("templateName", metrics.getTemplateName())
                .append("second", metrics.getSecond());
        BasicDBObject returnFields = new BasicDBObject();
        BasicDBObject sort = new BasicDBObject();
        boolean remove = false;
        BasicDBObject numberOfImages = new BasicDBObject("numberOfImages", metrics.getNumberOfImages());
        BasicDBObject sizeOfImages = new BasicDBObject("sizeOfImages", metrics.getSizeOfImages());
        BasicDBObject update = new BasicDBObject()
                .append("imagePlantId", metrics.getImagePlantId())
                .append("templateName", metrics.getTemplateName())
                .append("second", metrics.getSecond())
                .append("$inc", numberOfImages)
                .append("$inc", sizeOfImages);
        boolean returnNew = true;
        boolean upsert = true;
        coll.findAndModify(criteria, returnFields, sort, remove, update, returnNew, upsert);
    }
    
    private void updateTemplateMetrics(ImageMetricsOS metrics) {
        DBCollection coll = getDatabase().getCollection("ImageMetricsInTemplate");
        BasicDBObject criteria = new BasicDBObject()
                .append("imagePlantId", metrics.getImagePlantId())
                .append("templateName", metrics.getTemplateName());
        BasicDBObject returnFields = new BasicDBObject();
        BasicDBObject sort = new BasicDBObject();
        boolean remove = false;
        BasicDBObject numberOfImages = new BasicDBObject("numberOfImages", metrics.getNumberOfImages());
        BasicDBObject sizeOfImages = new BasicDBObject("sizeOfImages", metrics.getSizeOfImages());
        BasicDBObject update = new BasicDBObject()
                .append("imagePlantId", metrics.getImagePlantId())
                .append("templateName", metrics.getTemplateName())
                .append("$inc", numberOfImages)
                .append("$inc", sizeOfImages);
        boolean returnNew = true;
        boolean upsert = true;
        coll.findAndModify(criteria, returnFields, sort, remove, update, returnNew, upsert);
    }

    @Override
    public long calculateNumberOfImages(String imagePlantId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long calculateNumberOfImages(TemplateIdentity templateId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long calculateSizeOfImages(String imagePlantId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long calculateSizeOfImages(TemplateIdentity templateId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            String imagePlantId, TimeInterval interval) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginatedResult<List<ImageMetricsOS>> retrieveStats(
            TemplateIdentity templateId, TimeInterval interval) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private ImageMetricsOS createMetrics(ImageOS image) {
        long second = getSecond(image);
        return new ImageMetricsOS(
                image.getId().getImagePlantId(), 
                image.getVersion().getTemplateName(),
                second,
                1, 
                image.getMetadata().getSize());
    }

    private long getSecond(ImageOS image) {
        long time = image.getDateTime().getTime();
        long restOfSecond = time % 1000;
        long second = time - (restOfSecond);
        if (restOfSecond > 0) {
            second += 1; //move to next second.
        }
        return second;
    }

}
