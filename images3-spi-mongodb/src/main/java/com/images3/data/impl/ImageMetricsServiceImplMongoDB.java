/*******************************************************************************
 * Copyright 2014 Rui Sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.images3.data.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.ImageMetricsType;
import com.images3.common.TemplateIdentity;
import com.images3.common.TimeInterval;
import com.images3.data.ImageMetricsOS;
import com.images3.data.ImageOS;
import com.images3.data.spi.ImageMetricsService;
import com.images3.exceptions.NoSuchEntityFoundException;
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
    public void recordInbound(ImageOS image) {
        ImageMetricsOS metrics = createMetrics(image, true);
        updateSecondMetrics(metrics);
        updateTemplateMetrics(metrics);
    }
    
    @Override
    public void recordOutbound(ImageOS image) {
        ImageMetricsOS metrics = createMetrics(image, false);
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
        BasicDBObject increase = getImageIncrements(metrics);
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
        BasicDBObject increase = getImageIncrements(metrics);
        BasicDBObject update = new BasicDBObject()
            .append("$inc", increase);
        boolean returnNew = true;
        boolean upsert = true;
        coll.findAndModify(criteria, returnFields, sort, remove, update, returnNew, upsert);
    }
    
    private BasicDBObject getImageIncrements(ImageMetricsOS metrics) {
        BasicDBObject increase = new BasicDBObject();
        Map<ImageMetricsType, Long> numbers = metrics.getNumbers();
        for (Iterator<ImageMetricsType> iter=numbers.keySet().iterator(); iter.hasNext();) {
            ImageMetricsType type = iter.next();
            Long number = metrics.getNumbers().get(type);
            increase.append(type.toString(), number);
        }
        return increase;
    }

    @Override
    public long calculateNumber(String imagePlantId, ImageMetricsType type) {
        long counts = 0;
        List<DBObject> objects = selectMetricsByImagePlantId(imagePlantId);
        for (DBObject obj: objects) {
            BasicDBObject object = (BasicDBObject) obj;
            counts += object.getLong(type.toString());
        }
        return counts;
    }
    
    @Override
    public long calculateNumber(TemplateIdentity templateId, ImageMetricsType type) {
        BasicDBObject object = (BasicDBObject) selectMetricsByTempalteId(templateId);
        return object.getLong(type.toString());
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
            throw new NoSuchEntityFoundException(
                    "ImageMetrics", templateId.toString(), "No such metrics found.");
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
    
    private ImageMetricsOS createMetrics(ImageOS image, boolean isInbound) {
        long second = getSecond(System.currentTimeMillis());
        Map<ImageMetricsType, Long> stats = new HashMap<ImageMetricsType, Long>();
        if (isInbound) {
            stats.put(ImageMetricsType.COUNTS_INBOUND, 1L);
            stats.put(ImageMetricsType.SIZE_INBOUND, image.getMetadata().getSize());
            stats.put(ImageMetricsType.COUNTS_OUTBOUND, 0L);
            stats.put(ImageMetricsType.SIZE_OUTBOUND, 0L);
        } else {
            stats.put(ImageMetricsType.COUNTS_INBOUND, 0L);
            stats.put(ImageMetricsType.SIZE_INBOUND, 0L);
            stats.put(ImageMetricsType.COUNTS_OUTBOUND, 1L);
            stats.put(ImageMetricsType.SIZE_OUTBOUND, image.getMetadata().getSize());
        }
        return new ImageMetricsOS(
                image.getId().getImagePlantId(), 
                image.getVersion().getTemplateName(),
                second,
                stats);
    }

    private long getSecond(long time) {
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
            PageCursor cursor = selectPageCursorById((String) pageCursor);
            return getStatsByImagePlantId(imagePlantId, interval, cursor.getPage());
        }
        if ("retrieveStatsByTemplateId".equals(tag)) {
            TemplateIdentity templateIdentity = (TemplateIdentity) arguments[0];
            TimeInterval interval = (TimeInterval) arguments[1];
            PageCursor cursor = selectPageCursorById((String) pageCursor);
            return getStatsByTemplateId(templateIdentity, interval, cursor.getPage());
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
        return nextPageCursorId(tag, arguments, pageCursor, result);
    }
    
    private List<ImageMetricsOS> getStatsByImagePlantId(String imagePlantId,
            TimeInterval interval, Page cursor) {
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
            TimeInterval interval, Page cursor) {
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
        long startSecond = getSecond(interval.getStart().getTime());
        long endSecond = getSecond(interval.getEnd().getTime());
        return new long[] {startSecond, endSecond};
    }
    
    private List<ImageMetricsOS> getImageStats(BasicDBObject criteria, Page pageCursor) {
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
        return nextPageCursor(null).getId();
    }

    @Override
    public Object getPrevPageCursor(String tag, Object[] arguments, Object pageCursor,
            List<ImageMetricsOS> result) {
        return previousPageCursorId(tag, arguments, pageCursor, result);
    }

}
