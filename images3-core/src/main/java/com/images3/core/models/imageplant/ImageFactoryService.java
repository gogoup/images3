package com.images3.core.models.imageplant;

import java.io.File;
import java.util.Date;

import com.images3.ImageIdentity;
import com.images3.ImageMetadata;
import com.images3.UnknownImageFormatException;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageProcessor;

public class ImageFactoryService {
    
    private ImageAccess imageAccess;
    private ImageProcessor imageProcessor;
    private VersionFactoryService factoryService;
    
    public ImageFactoryService(ImageAccess imageAccess, ImageProcessor imageProcessor,
            VersionFactoryService factoryService) {
        this.imageAccess = imageAccess;
        this.imageProcessor = imageProcessor;
        this.factoryService = factoryService;
    }

    public ImageEntity generateImage(ImagePlantRoot imagePlant, File imageFile) {
        String id = imageAccess.generateImageId(imagePlant.getObjectSegment());
        if (!imageProcessor.isSupportedFormat(imageFile)) {
            throw new UnknownImageFormatException(id);
        }
        return generateImage(id, imagePlant, imageFile);
    }
    
    public ImageEntity generateImage(ImagePlantRoot imagePlant, ImageEntity image, 
            TemplateEntity template) {
        String id = imageAccess.generateImageId(imagePlant.getObjectSegment());
        File imageContent = imageProcessor.resizeImage(
                id, image.getObjectSegment(), image.getContent(), template.getResizingConfig());
        return generateImage(id, imagePlant, imageContent);
    }
    
    private ImageEntity generateImage(String imageId, ImagePlantRoot imagePlant, File imageContent) {
        Date dateTime = new Date(System.currentTimeMillis());
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        ImageOS objectSegment = new ImageOS(
                new ImageIdentity(imagePlant.getId(), imageId), dateTime, metadata);
        ImageEntity entity = reconstituteImage(imagePlant, objectSegment, imageContent, null, null);
        entity.markAsNew();
        return entity;
    }
    
    public ImageEntity reconstituteImage(ImagePlantRoot imagePlant, ImageOS objectSegment, File imageContent,
            ImageRepositoryService imageRepository, VersionRepositoryService versionRepository) {
        if (null == objectSegment) {
            return null;
        }
        return new ImageEntity(
                imagePlant, objectSegment, imageContent, imageRepository, factoryService, versionRepository);
    }
    
}
