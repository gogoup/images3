package com.images3.core.models.imageplant;

import com.images3.ImageIdentity;
import com.images3.VersionIdentity;
import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.infrastructure.VersionOS;

public class VersionFactoryService {
    
    public VersionFactoryService() {}

    public VersionEntity generateVersion(ImageEntity image, 
            Template template, Image versioningImage) {
        VersionIdentity id =new VersionIdentity(
                new ImageIdentity(
                        image.getImagePlant().getId(), image.getId()), template.getId());
        VersionOS objectSegment = new VersionOS(id, versioningImage.getId());
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
