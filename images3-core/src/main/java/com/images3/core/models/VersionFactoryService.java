package com.images3.core.models;

import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.infrastructure.data.VersionOS;

public class VersionFactoryService {
    
    public VersionFactoryService() {
    }

    public VersionEntity generateVersion(Image image, 
            Template template, Image versioningImage) {
        VersionOS objectSegment = new VersionOS(image.getId(), 
                template.getId(), versioningImage.getId());
        VersionEntity entity = reconstituteVersion(image, objectSegment, 
                template, versioningImage, null, null);
        entity.markAsNew();
        return entity;
    }
    
    public VersionEntity reconstituteVersion(Image image, 
            VersionOS objectSegment, Template template, Image versioningImage,
            TemplateRepositoryService templateRepository, 
            ImageRepositoryService imageRepository) {
        if (null == objectSegment) {
            return null;
        }
        return new VersionEntity(image, objectSegment, template, 
                versioningImage, templateRepository, imageRepository);
    }
    
 }
