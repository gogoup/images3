package com.images3.core.models.imageplant;

import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.VersionOS;
import com.images3.utility.DirtyMark;

public class VersionEntity extends DirtyMark implements Version {
    
    private Image image;
    private VersionOS objectSegment;
    private Template template;
    private Image versioningImage;
    private TemplateRepositoryService templateRepository;
    private ImageRepositoryService imageRepository;
    
    public VersionEntity(Image image, VersionOS objectSegment,
            Template template, Image versioningImage,
            TemplateRepositoryService templateRepository, 
            ImageRepositoryService imageRepository) {
        this.image = image;
        this.objectSegment = objectSegment;
        this.template = template;
        this.versioningImage = versioningImage;
        this.templateRepository = templateRepository;
        this.imageRepository = imageRepository;
    }
    
    public VersionOS getObjectSegment() {
        return objectSegment;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public Template getTemplate() {
        if (null == template) {
            template = templateRepository.findTemplateById(
                    (ImagePlantRoot) image.getImagePlant(), 
                    getObjectSegment().getId().getTemplateId());
        }
        return template;
    }

    @Override
    public Image getVesioningImage() {
        if (null == versioningImage) {
            versioningImage = imageRepository.findImageById(
                    (ImagePlantRoot) image.getImagePlant(), 
                    getObjectSegment().getVesioningImageId());
        }
        return versioningImage;
    }

}
