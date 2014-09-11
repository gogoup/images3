package com.images3.core.infrastructure;

import java.util.Date;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ImageDimension;
import com.images3.common.ImageFormat;
import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetadata;
import com.images3.common.ImageVersion;
import com.images3.common.ResizingConfig;
import com.images3.common.ResizingUnit;
import com.images3.common.TemplateIdentity;
import com.mongodb.BasicDBObject;

public class MongoDBObjectMapper {
    
    public BasicDBObject mapToBasicDBObject(String id, PageCursor source) {
        return new BasicDBObject()
            .append("id", id)
            .append("start", source.getStart())
            .append("size", source.getSize())
            .append("creationTime", System.currentTimeMillis());
    }
    
    public PageCursor mapToPageCursor(BasicDBObject source) {
        return new PageCursor(
                source.getInt("start"),
                source.getInt("size"));
    }

    public BasicDBObject mapToBasicDBObject(ImagePlantOS source) {
        return new BasicDBObject()
            .append("id", source.getId())
            .append("name", source.getName())
            .append("creationTime", source.getCreationTime().getTime())
            .append("bucket", mapToBasicDBObject(source.getAmazonS3Bucket()))
            .append("masterTemplateName", source.getMasterTemplateName())
            .append("numberOfTemplates", source.getNumberOfTemplates());
    }
    
    public ImagePlantOS mapToImagePlantOS(BasicDBObject source) {
        return new ImagePlantOS(
                source.getString("id"),
                source.getString("name"),
                new Date(source.getLong("creationTime")),
                mapToAmazonS3Bucket((BasicDBObject) source.get("bucket")),
                source.getString("masterTemplateName"),
                source.getLong("numberOfTemplates"));
    }
    
    public BasicDBObject mapToBasicDBObject(AmazonS3Bucket source) {
        return new BasicDBObject()
            .append("accessKey", source.getAccessKey())
            .append("secretKey", source.getSecretKey())
            .append("name", source.getName());
    }
    
    public AmazonS3Bucket mapToAmazonS3Bucket(BasicDBObject source) {
        return new AmazonS3Bucket(
                source.getString("accessKey"),
                source.getString("secretKey"),
                source.getString("name"));
    }
    
    public BasicDBObject mapToBasicDBObject(TemplateOS source) {
        return new BasicDBObject()
            .append("imagePlantId", source.getId().getImagePlantId())
            .append("name", source.getId().getTemplateName())
            .append("isArchived", source.isArchived())
            .append("isRemovable", source.isRemovable())
            .append("resizingConfig", mapToBasicDBObject(source.getResizingConfig()));
    }
    
    public TemplateOS mapToTemplateOS(BasicDBObject source) {
        return new TemplateOS(
                new TemplateIdentity(
                        source.getString("imagePlantId"),
                        source.getString("name")),
                source.getBoolean("isArchived"),
                source.getBoolean("isRemovable"),
                mapToResizingConfig((BasicDBObject) source.get("resizingConfig")));
    }
    
    public BasicDBObject mapToBasicDBObject(ResizingConfig source) {
        return new BasicDBObject()
            .append("height", source.getHeight())
            .append("width", source.getWidth())
            .append("unit", source.getUnit().toString())
            .append("isKeepProportions", source.isKeepProportions());
    }
    
    public ResizingConfig mapToResizingConfig(BasicDBObject source) {
        return new ResizingConfig(
                ResizingUnit.valueOf(source.getString("unit")),
                source.getInt("width"),
                source.getInt("height"),
                source.getBoolean("isKeepProportions"));
    }
    
    public BasicDBObject mapToBasicDBObject(ImageOS source) {
        BasicDBObject obj = new BasicDBObject()
            .append("imagePlantId", source.getId().getImagePlantId())
            .append("id", source.getId().getImageId())
            .append("dateTime", source.getDateTime().getTime())
            .append("metadata", mapToBasicDBObject(source.getMetadata()))
            .append("version", mapToBasicDBObject(source.getVersion()));
        return obj;
    }
    
    public ImageOS mapToImageOS(BasicDBObject source) {
        return new ImageOS(
                new ImageIdentity(
                        source.getString("imagePlantId"), 
                        source.getString("id")),
                new Date(source.getLong("dateTime")),
                mapToImageMetadata((BasicDBObject) source.get("metadata")),
                mapToImageVersion((BasicDBObject) source.get("version")));
    }
    
    public BasicDBObject mapToBasicDBObject(ImageMetadata source) {
        return new BasicDBObject()
            .append("dimension", mapToBasicDBObject(source.getDimension()))
            .append("format", source.getFormat().toString())
            .append("size", source.getSize());
    }
    
    public ImageMetadata mapToImageMetadata(BasicDBObject source) {
        return new ImageMetadata(
                mapToImageDimension((BasicDBObject) source.get("dimension")),
                ImageFormat.valueOf(source.getString("format")),
                source.getLong("size"));
    }
    
    public BasicDBObject mapToBasicDBObject(ImageDimension source) {
        return new BasicDBObject()
            .append("height", source.getHeight())
            .append("width", source.getWidth());
    }
    
    public ImageDimension mapToImageDimension(BasicDBObject source) {
        return new ImageDimension(
                source.getInt("width"),
                source.getInt("height"));
    }
    
    public BasicDBObject mapToBasicDBObject(ImageVersion source) {
        return new BasicDBObject()
            .append("templateName", source.getTemplateName())
            .append("originalImageId", source.getOriginalImageId());
    }
    
    public ImageVersion mapToImageVersion(BasicDBObject source) {
        return new ImageVersion(
                source.getString("templateName"), 
                source.getString("originalImageId"));
    }
    
    public ImageMetricsOS mapToImageMetricsOS(BasicDBObject source) {
        return new ImageMetricsOS(
                source.getString("imagePlantId"),
                source.getString("templateName"),
                source.getLong("second"),
                source.getLong("numberOfImages"),
                source.getLong("sizeOfImages"));
    }
    
}
