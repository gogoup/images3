package com.images3.core.models.imageplant;

import java.io.File;
import java.util.Date;

import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.utility.DirtyMark;

public class ImageEntity extends DirtyMark implements Image {
    
    private ImagePlantRoot imagePlant;
    private ImageOS objectSegment;
    private File imageContent;
    private ImageRepositoryService imageRepository;
    private TemplateRepositoryService templateRepository;
    private Version version;
    
    public ImageEntity(ImagePlantRoot imagePlant, ImageOS objectSegment,
            File imageContent, ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository, Version version) {
        this.imagePlant = imagePlant;
        this.objectSegment = objectSegment;
        this.imageContent = imageContent;
        this.imageRepository = imageRepository;
        this.templateRepository = templateRepository;
        this.version = version;
    }
    
    public ImageOS getObjectSegment() {
        return objectSegment;
    }

    @Override
    public ImagePlant getImagePlant() {
        return imagePlant;
    }

    @Override
    public String getId() {
        return getObjectSegment().getId().getImageId();
    }

    @Override
    public File getContent() {
        if (null == imageContent) {
            imageContent = imageRepository.findImageContent(this);
        }
        return imageContent;
    }

    @Override
    public Date getDateTime() {
        return getObjectSegment().getDateTime();
    }

    
    @Override
    public Version getVersion() {
        if (null == version 
                && null != getObjectSegment().getVersion()) {
            Image originalImage = imageRepository.findImageById(
                    imagePlant, getObjectSegment().getVersion().getOriginalImageId());
            Template template = templateRepository.findTemplateByName(
                    imagePlant, getObjectSegment().getVersion().getTemplateName());
            version = new Version(template, originalImage);
        }
        return version;
    }

}
