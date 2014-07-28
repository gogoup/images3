package com.images3.core.models;

import java.util.Date;
import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.infrastructure.data.ImagePlantOS;

public class ImagePlantRoot implements ImagePlant {
    
    private ImagePlantOS objectSegment;
    private ImageFactoryService imageFactory;
    private ImageRepositoryService imageRepository;
    private TemplateFactoryService templateFactory;
    private TemplateRepositoryService templateRepository;
    private VersionFactoryService versionFactory;
    private VersionRepositoryService versionRepository;
    
    public ImagePlantRoot(ImagePlantOS objectSegment, ImageFactoryService imageFactory,
            ImageRepositoryService imageRepository, TemplateFactoryService templateFactory,
            TemplateRepositoryService templateRepository, VersionFactoryService versionFactory, 
            VersionRepositoryService versionRepository) {
        this.objectSegment = objectSegment;
        this.imageFactory = imageFactory;
        this.imageRepository = imageRepository;
        this.templateFactory = templateFactory;
        this.templateRepository = templateRepository;
        this.versionFactory= versionFactory;
        this.versionRepository = versionRepository;
    }
    
    public ImagePlantOS getObjectSegment() {
        return objectSegment;
    }

    @Override
    public String getId() {
        return getObjectSegment().getId();
    }

    @Override
    public String getName() {
        return getObjectSegment().getName();
    }

    @Override
    public Date getCreationTime() {
        return getObjectSegment().getCreationTime();
    }

    @Override
    public Template createTemplate(String name, ImageSize scalingSize,
            boolean isKeepProportions) {
        return null;
    }

    @Override
    public void updateTemplate(Template template) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeTemplate(Template template) {
    }

    @Override
    public Template fetchTemplateById(String id) {
        return null;
    }

    @Override
    public PaginatedResult<List<Template>> listAllTemplates() {
        return null;
    }

    @Override
    public Image createImage(String base64Image) {
        return null;
    }

    @Override
    public Image createImage(Image image, Template template) {
        return null;
    }

    @Override
    public Image fetchImageById(String id) {
        return null;
    }

    @Override
    public PaginatedResult<List<Image>> listAllImages() {
        return null;
    }

    @Override
    public void removeImage(Image image) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeImageAndVersions(Image image) {
        // TODO Auto-generated method stub
        
    }

}
