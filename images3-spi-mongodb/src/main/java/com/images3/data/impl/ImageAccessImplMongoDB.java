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
import java.util.List;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageVersion;
import com.images3.data.ImageOS;
import com.images3.data.ImagePlantOS;
import com.images3.data.spi.ImageAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class ImageAccessImplMongoDB extends MongoDBAccess<ImageOS> implements ImageAccess {

    public ImageAccessImplMongoDB(MongoClient mongoClient, String dbname,
            MongoDBObjectMapper objectMapper, int pageSize) {
        super(mongoClient, dbname, objectMapper, pageSize);
    }

    @Override
    public boolean isDuplicateVersion(String imagePlantId, ImageVersion version) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("version.templateName", version.getTemplateName())
                                    .append("version.originalImageId", version.getOriginalImageId());  
        DBCursor cursor = coll.find(criteria);
        return cursor.hasNext();
    }

    public String generateImageId(ImagePlantOS imagePlant) {
        return ShortUUID.randomUUID();
    }

    public void insertImage(ImageOS image) {
        DBCollection coll = getDatabase().getCollection("Image");
        coll.insert(getObjectMapper().mapToBasicDBObject(image));
    }

    public void deleteImage(ImageOS image) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", image.getId().getImagePlantId())
                                    .append("id", image.getId().getImageId());
        WriteResult result = coll.remove(criteria);
        checkForAffectedDocuments(result, 1);
    }

    @Override
    public void deleteImages(String imagePlantId) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId);
        coll.remove(criteria);
    }

    public ImageOS selectImageById(ImageIdentity id) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", id.getImagePlantId())
                                    .append("id", id.getImageId());
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToImageOS((BasicDBObject) cursor.next());
    }

    @Override
    public ImageOS selectImageByVersion(String imagePlantId, ImageVersion version) {
        DBCollection coll = getDatabase().getCollection("Image");
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("version.templateName", version.getTemplateName())
                                    .append("version.originalImageId", version.getOriginalImageId());
        DBCursor cursor = coll.find(criteria);
        if (!cursor.hasNext()) {
            return null;
        }
        return getObjectMapper().mapToImageOS((BasicDBObject) cursor.next());
    }

    @Override
    public PaginatedResult<List<ImageOS>> selectImagesByOriginalImageId(
            String imagePlantId, String originalImageId) {
        return new PaginatedResult<List<ImageOS>>(
                this, "getImagesByOriginalImageId", new Object[] {imagePlantId, originalImageId});
    }

    @Override
    public PaginatedResult<List<ImageOS>> selectImagesByTemplateName(
            String imagePlantId, String templateName) {
        return new PaginatedResult<List<ImageOS>>(
                this, "getImagesByTemplateName", new Object[] {imagePlantId, templateName});
    }

    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(
            String imagePlantId) {
        return new PaginatedResult<List<ImageOS>>(
                this, "getImagesByImagePlantId", new Object[] {imagePlantId});
    }
    
    public List<ImageOS> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getImagesByOriginalImageId".equals(tag)) {
            String imagePlantId = (String) arguments[0];
            String originalImageId = (String) arguments[1];
            PageCursor cursor = selectPageCursorById((String) pageCursor);
            return getImagesByOriginalImageId(imagePlantId, originalImageId, cursor.getPage());
        }
        if ("getImagesByImagePlantId".equals(tag)) {
            String imagePlantId = (String) arguments[0];
            PageCursor cursor = selectPageCursorById((String) pageCursor);
            return getImagesByImagePlantId(imagePlantId, cursor.getPage());
        }
        if ("getImagesByTemplateName".equals(tag)) {
            String imagePlantId = (String) arguments[0];
            String templateName = (String) arguments[1];
            PageCursor cursor = selectPageCursorById((String) pageCursor);
            return getImagesByTemplateName(imagePlantId, templateName, cursor.getPage());
        }
        throw new UnsupportedOperationException(tag);
    }
   
    @Override
    public boolean isFetchAllResultsSupported(String tag, Object[] arguments) {
        if ("getImagesByOriginalImageId".equals(tag)) {
            return false;
        }
        if ("getImagesByImagePlantId".equals(tag)) {
            return false;
        }
        if ("getImagesByTemplateName".equals(tag)) {
            return false;
        }
        throw new UnsupportedOperationException(tag);
    }

    @Override
    public List<ImageOS> fetchAllResults(String tag, Object[] arguments) {
        throw new UnsupportedOperationException(tag);
    }

    private List<ImageOS> getImagesByOriginalImageId(String imagePlantId, 
            String originalImageId, Page pageCursor) {
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("version.originalImageId", originalImageId);
        return getImages(criteria, pageCursor);
    }

    private List<ImageOS> getImagesByImagePlantId(String imagePlantId, 
            Page pageCursor) {
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId);
        return getImages(criteria, pageCursor);
    }
    
    private List<ImageOS> getImagesByTemplateName(String imagePlantId, 
            String templateName, Page pageCursor) {
        BasicDBObject criteria = new BasicDBObject()
                                    .append("imagePlantId", imagePlantId)
                                    .append("version.templateName", templateName);
        return getImages(criteria, pageCursor);
    }
    
    private List<ImageOS> getImages(BasicDBObject criteria, Page pageCursor) {
        DBCollection coll = getDatabase().getCollection("Image");
        int skipRecords = (pageCursor.getStart() - 1) * pageCursor.getSize();
        List<DBObject> objects = coll.find(criteria).skip(skipRecords).limit(pageCursor.getSize()).toArray();
        List<ImageOS> images = new ArrayList<ImageOS>(objects.size());
        for (DBObject obj: objects) {
            images.add(getObjectMapper().mapToImageOS((BasicDBObject) obj));
        }
        return images;
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<ImageOS> result) {
        return nextPageCursorId(tag, arguments, pageCursor, result);
    }

    @Override
    public Object getFirstPageCursor(String tag, Object[] arguments) {
        return nextPageCursor(null).getId();
    }

    @Override
    public Object getPrevPageCursor(String tag, Object[] arguments, Object pageCursor,
            List<ImageOS> result) {
        return previousPageCursorId(tag, arguments, pageCursor, result);
    }
}
