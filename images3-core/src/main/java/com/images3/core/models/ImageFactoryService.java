package com.images3.core.models;

import java.io.File;
import java.util.Date;

import com.images3.common.ImageMetadata;
import com.images3.core.Image;
import com.images3.core.infrastructure.ImageProcessor;
import com.images3.core.infrastructure.data.ImageOS;
import com.images3.core.infrastructure.data.spi.ImageAccess;

public class ImageFactoryService {
    
    private ImageAccess imageAccess;
    private ImageProcessor imageProcessor;
    
    public ImageFactoryService(ImageAccess imageAccess, ImageProcessor imageProcessor) {
        this.imageAccess = imageAccess;
        this.imageProcessor = imageProcessor;
    }

    public ImageEntity generateImage(ImagePlantRoot imagePlant, String base64Image) {
        File imageFile = imageProcessor.convertToImageFile(base64Image);
        return generateImage(imagePlant, imageFile);
    }
    
    public ImageEntity generateImage(ImagePlantRoot imagePlant, Image image, 
            TemplateEntity template) {
        File imageFile = imageProcessor.resizeImage(
                image.getContent(), template.getResizingConfig());
        return generateImage(imagePlant, imageFile);
    }
    
    private ImageEntity generateImage(ImagePlantRoot imagePlant, File imageFile) {
        String id = imageAccess.generateImageId(imagePlant.getObjectSegment());
        Date dateTime = new Date(System.currentTimeMillis());
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageFile);
        ImageOS objectSegment = new ImageOS(
                imagePlant.getId(), id, imageFile, dateTime, metadata);
        ImageEntity entity = reconstituteImage(imagePlant, objectSegment, null);
        entity.markAsNew();
        return entity;
    }
    
    public ImageEntity reconstituteImage(ImagePlantRoot imagePlant, ImageOS objectSegment, 
            VersionRepositoryService versionRepository) {
        if (null == objectSegment) {
            return null;
        }
        return new ImageEntity(imagePlant, objectSegment, versionRepository);
    }
    
}
