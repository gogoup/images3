package com.images3.core.models.imageplant;

import java.io.File;
import java.util.Date;

import com.images3.DuplicateImageVersionException;
import com.images3.ImageIdentity;
import com.images3.ImageMetadata;
import com.images3.UnknownImageFormatException;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.VersionOS;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageProcessor;

public class ImageFactoryService {
    
    private ImageAccess imageAccess;
    private ImageProcessor imageProcessor;
    
    public ImageFactoryService(ImageAccess imageAccess, ImageProcessor imageProcessor) {
        this.imageAccess = imageAccess;
        this.imageProcessor = imageProcessor;
    }

    public ImageEntity generateImage(ImagePlantRoot imagePlant, File imageFile, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        String id = imageAccess.generateImageId(imagePlant.getObjectSegment());
        if (!imageProcessor.isSupportedFormat(imageFile)) {
            throw new UnknownImageFormatException(id);
        }
        return generateImage(
                id, imagePlant, imageFile, imageRepository, templateRepository, null);
    }
    
    public ImageEntity generateImage(ImagePlantRoot imagePlant, ImageEntity originalImage, 
            TemplateEntity template, ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository) {
        if (imageAccess.isDuplicateVersion(
                new VersionOS(template.getName(), originalImage.getId()))) {
            throw new DuplicateImageVersionException(
                    template.getName(), originalImage.getId());
        }
        String id = imageAccess.generateImageId(imagePlant.getObjectSegment());
        File imageContent = imageProcessor.resizeImage(
                id, originalImage.getObjectSegment(), originalImage.getContent(), template.getResizingConfig());
        Version version = new Version(template, originalImage);
        return generateImage(
                id, imagePlant,  imageContent, imageRepository, templateRepository, version);
    }
    
    private ImageEntity generateImage(String imageId, ImagePlantRoot imagePlant, File imageContent, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository, Version version) {
        Date dateTime = new Date(System.currentTimeMillis());
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        VersionOS versionOS = null;
        if (null != version) {
            versionOS = new VersionOS(version.getTemplate().getName(), version.getOriginalImage().getId());
        }
        ImageOS objectSegment = new ImageOS(
                new ImageIdentity(imagePlant.getId(), imageId), dateTime, metadata, versionOS);
        ImageEntity entity = reconstituteImage(
                imagePlant, objectSegment, imageContent, imageRepository, templateRepository, version);
        entity.markAsNew();
        return entity;
    }
    
    public ImageEntity reconstituteImage(ImagePlantRoot imagePlant, ImageOS objectSegment, File imageContent,
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository, Version version) {
        if (null == objectSegment) {
            return null;
        }
        return new ImageEntity(
                imagePlant, objectSegment, imageContent, imageRepository, templateRepository, version);
    }
    
}
