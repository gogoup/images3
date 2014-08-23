package com.images3.core.models.imageplant;

import java.io.File;
import java.util.Date;

import org.mockito.Mockito;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ImageDimension;
import com.images3.common.ImageFormat;
import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetadata;
import com.images3.common.ResizingConfig;
import com.images3.common.ResizingUnit;
import com.images3.common.TemplateIdentity;
import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.TemplateOS;

public class SetupHelper {

    public static AmazonS3Bucket setupAmazonS3Bucket(String accessKey, 
            String secretKey, String name) {
        AmazonS3Bucket amazonS3Bucket = Mockito.mock(AmazonS3Bucket.class);
        Mockito.when(amazonS3Bucket.getAccessKey()).thenReturn(accessKey);
        Mockito.when(amazonS3Bucket.getSecretKey()).thenReturn(secretKey);
        Mockito.when(amazonS3Bucket.getName()).thenReturn(name);
        return amazonS3Bucket;
    }
    
    public static ResizingConfig setupResizingConfig(ResizingUnit unit, 
            int width, int height, boolean isKeepProportions) {
        ResizingConfig resizingConfig = Mockito.mock(ResizingConfig.class);
        Mockito.when(resizingConfig.getUnit()).thenReturn(unit);
        Mockito.when(resizingConfig.getWidth()).thenReturn(width);
        Mockito.when(resizingConfig.getHeight()).thenReturn(height);
        Mockito.when(resizingConfig.isKeepProportions()).thenReturn(isKeepProportions);
        return resizingConfig;
    }
    
    public static ImagePlantOS setupImagePlantOS(String id, String name, 
            Date creationTime, AmazonS3Bucket bucket) {
        ImagePlantOS objectSegment = Mockito.mock(ImagePlantOS.class);
        Mockito.when(objectSegment.getId()).thenReturn(id);
        Mockito.when(objectSegment.getName()).thenReturn(name);
        Mockito.when(objectSegment.getCreationTime()).thenReturn(creationTime);
        Mockito.when(objectSegment.getAmazonS3Bucket()).thenReturn(bucket);
        return objectSegment;
    }
    
    public static TemplateOS setupTemplateOS(TemplateIdentity id, 
            boolean isArchived, boolean isRemovable, ResizingConfig resizingConfig) {
        TemplateOS objectSegment = Mockito.mock(TemplateOS.class);
        Mockito.when(objectSegment.getId()).thenReturn(id);
        Mockito.when(objectSegment.isArchived()).thenReturn(isArchived);
        Mockito.when(objectSegment.isRemovable()).thenReturn(isRemovable);
        Mockito.when(objectSegment.getResizingConfig()).thenReturn(resizingConfig);
        return objectSegment;
    }
    
    public static TemplateEntity setupTemplateEntity(ImagePlantRoot imagePlant, TemplateOS objectSegment) {
        TemplateEntity template = Mockito.mock(TemplateEntity.class);
        Mockito.when(template.getImagePlant()).thenReturn(imagePlant);
        Mockito.when(template.getObjectSegment()).thenReturn(objectSegment);
        String name = objectSegment.getId().getTemplateName();
        Mockito.when(template.getName()).thenReturn(name);
        boolean isArchived = objectSegment.isArchived();
        Mockito.when(template.isArchived()).thenReturn(isArchived);
        boolean isRemovable = objectSegment.isRemovable();
        Mockito.when(template.isRemovable()).thenReturn(isRemovable);
        ResizingConfig confg = objectSegment.getResizingConfig();
        Mockito.when(template.getResizingConfig()).thenReturn(confg);
        return template;
    }
    
    public static ImageIdentity setupImageIdentity(String imagePlantId, String imageId) {
        ImageIdentity objectSegment = Mockito.mock(ImageIdentity.class);
        Mockito.when(objectSegment.getImagePlantId()).thenReturn(imagePlantId);
        Mockito.when(objectSegment.getImageId()).thenReturn(imageId);
        return objectSegment;
    }
    
    public static ImageDimension setupImageDimension(int width, int height) {
        ImageDimension objectSegment = Mockito.mock(ImageDimension.class);
        Mockito.when(objectSegment.getWidth()).thenReturn(width);
        Mockito.when(objectSegment.getHeight()).thenReturn(height);
        return objectSegment;
    }
    
    public static ImageMetadata setupImageMetadata(ImageDimension dimension, 
            ImageFormat format, long size) {
        ImageMetadata objectSegment = Mockito.mock(ImageMetadata.class);
        Mockito.when(objectSegment.getDimension()).thenReturn(dimension);
        Mockito.when(objectSegment.getFormat()).thenReturn(format);
        Mockito.when(objectSegment.getSize()).thenReturn(size);
        return objectSegment;
    }
    
    public static ImageOS setupImageOS(ImageIdentity id, Date dateTime, 
            ImageMetadata metadata) {
        ImageOS objectSegment = Mockito.mock(ImageOS.class);
        Mockito.when(objectSegment.getId()).thenReturn(id);
        Mockito.when(objectSegment.getDateTime()).thenReturn(dateTime);
        Mockito.when(objectSegment.getMetadata()).thenReturn(metadata);
        return objectSegment;
    }
    
    public static ImageEntity setupImageEntity(ImagePlantRoot imagePlant, 
            ImageOS objectSegment, File imageContent, Version version) {
        ImageEntity image = Mockito.mock(ImageEntity.class);
        Mockito.when(image.getImagePlant()).thenReturn(imagePlant);
        Mockito.when(image.getObjectSegment()).thenReturn(objectSegment);
        String id = objectSegment.getId().getImageId();
        Mockito.when(image.getId()).thenReturn(id);
        Mockito.when(image.getContent()).thenReturn(imageContent);
        Date dateTime = objectSegment.getDateTime();
        Mockito.when(image.getDateTime()).thenReturn(dateTime);
        Mockito.when(image.getVersion()).thenReturn(version);
        return image;
    }
    
    public static Version setupVersion(Template template, Image originalImage) {
        Version version = Mockito.mock(Version.class);
        Mockito.when(version.getTemplate()).thenReturn(template);
        Mockito.when(version.getOriginalImage()).thenReturn(originalImage);
        return version;
    }
    
}
