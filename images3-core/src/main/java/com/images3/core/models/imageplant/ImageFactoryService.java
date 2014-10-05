package com.images3.core.models.imageplant;

import java.io.File;
import java.util.Date;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetadata;
import com.images3.common.ImageVersion;
import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageProcessor;
import com.images3.exceptions.DuplicateImageVersionException;
import com.images3.exceptions.UnknownImageFormatException;

public class ImageFactoryService {
    
    private ImageAccess imageAccess;
    private ImageProcessor imageProcessor;
    
    public ImageFactoryService(ImageAccess imageAccess, ImageProcessor imageProcessor) {
        this.imageAccess = imageAccess;
        this.imageProcessor = imageProcessor;
    }

    public ImageEntity generateImage(ImagePlantRoot imagePlant, File imageContent, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        String id = imageAccess.generateImageId(imagePlant.getObjectSegment());
        if (!imageProcessor.isSupportedFormat(imageContent)) {
            throw new UnknownImageFormatException(id);
        }
        TemplateEntity template = (TemplateEntity) imagePlant.getMasterTemplate();
        Version version = new Version(template, null);
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        File resizedContent = imageProcessor.resizeImage(
                metadata, 
                imageContent, 
                version.getTemplate().getResizingConfig());
        return generateImage(
                imagePlant, 
                resizedContent,
                version,
                imageRepository, 
                templateRepository);
    }
    
    public ImageEntity generateImage(ImagePlantRoot imagePlant, Version version, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        checkForOriginalImage(version);
        checkForArchivedTemplate(version.getTemplate());
        checkForMasterVersionGeneration(version);
        checkForDuplicateVersion(imagePlant, version);
        ImageEntity originalImage = (ImageEntity) version.getOriginalImage();
        File resizedContent = imageProcessor.resizeImage(
                originalImage.getObjectSegment().getMetadata(), 
                originalImage.getContent(), 
                version.getTemplate().getResizingConfig());
        return generateImage(
                imagePlant,  
                resizedContent, 
                version,
                imageRepository,
                templateRepository);
    }
    
    private void checkForArchivedTemplate(Template template) {
        if (template.isArchived()) {
            throw new IllegalArgumentException("Template " + template.getName() + " has been archived.");
        }
    }
    
    private void checkForOriginalImage(Version version) {
        if (null == version.getOriginalImage()) {
            throw new NullPointerException("Original image.");
        }
    }
    
    private void checkForMasterVersionGeneration(Version version) {
        if (version.getTemplate().getName().equalsIgnoreCase(TemplateEntity.MASTER_TEMPLATE_NAME)) {
            throw new UnsupportedOperationException(
                    "Generate a " + TemplateEntity.MASTER_TEMPLATE_NAME 
                            + " version of image from another image {"
                            + version.getOriginalImage().getId() + "} is not supported");
        }
    }
    
    private void checkForDuplicateVersion(ImagePlantRoot imagePlant, Version version) {
        if (imageAccess.isDuplicateVersion(
                imagePlant.getId(), new ImageVersion(
                        version.getTemplate().getName(), version.getOriginalImage().getId()))) {
            throw new DuplicateImageVersionException(
                    version.getTemplate().getName(), version.getOriginalImage().getId());
        }
    }
    
    private ImageEntity generateImage(ImagePlantRoot imagePlant, File imageContent, Version version,
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        ImageVersion versionOS = getVersionOS(version);
        String imageId = imageAccess.generateImageId(imagePlant.getObjectSegment());
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        ImageOS objectSegment = generateImageOS(
                imageId, metadata, imagePlant.getObjectSegment(), versionOS);
        ImageEntity entity = reconstituteImage(
                imagePlant, objectSegment, imageContent, imageRepository, templateRepository, version);
        entity.markAsNew();
        return entity;
    }
    
    private ImageVersion getVersionOS(Version version) {
        Template template = version.getTemplate();
        Image originalImage = version.getOriginalImage();
        ImageVersion imageVer = null;
        if (null != originalImage) {
            imageVer = new ImageVersion(template.getName(), originalImage.getId());
        } else {
            imageVer = new ImageVersion(template.getName(), null);
        }
        return imageVer;
    }
    
    private ImageOS generateImageOS(String id, ImageMetadata metadata, 
            ImagePlantOS imagePlantOS, ImageVersion versionOS) {
        Date dateTime = new Date(System.currentTimeMillis());
        ImageOS objectSegment = new ImageOS(
                new ImageIdentity(imagePlantOS.getId(), id), dateTime, metadata, versionOS);
        return objectSegment;
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
