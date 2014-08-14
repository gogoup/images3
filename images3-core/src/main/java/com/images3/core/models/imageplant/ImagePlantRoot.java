package com.images3.core.models.imageplant;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.images3.AmazonS3Bucket;
import com.images3.ResizingConfig;
import com.images3.UnremovableTemplateException;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.utility.DirtyMark;

import org.gogoup.dddutils.pagination.PaginatedResult;

public class ImagePlantRoot extends DirtyMark implements ImagePlant {
    
    private ImagePlantOS objectSegment;
    private ImageFactoryService imageFactory;
    private ImageRepositoryService imageRepository;
    private TemplateFactoryService templateFactory;
    private TemplateRepositoryService templateRepository;
    private VersionRepositoryService versionRepository;
    private Map<String, TemplateEntity> dirtyTemplates;
    private Map<String, ImageEntity> dirtyImages;
    
    public ImagePlantRoot() {}
    
    public ImagePlantRoot(ImagePlantOS objectSegment, ImageFactoryService imageFactory,
            ImageRepositoryService imageRepository, TemplateFactoryService templateFactory,
            TemplateRepositoryService templateRepository, VersionRepositoryService versionRepository) {
        this.objectSegment = objectSegment;
        this.imageFactory = imageFactory;
        this.imageRepository = imageRepository;
        this.templateFactory = templateFactory;
        this.templateRepository = templateRepository;
        this.versionRepository = versionRepository;
        this.dirtyTemplates = new HashMap<String, TemplateEntity>();
        this.dirtyImages = new HashMap<String, ImageEntity>();
    }
    
    public ImagePlantOS getObjectSegment() {
        return objectSegment;
    }
    
    void addDirtyTemplate(TemplateEntity template) {
        dirtyTemplates.put(template.getId(), template);
    }
    
    public List<TemplateEntity> getDirtyTemplates() {
        return new ArrayList<TemplateEntity>(dirtyTemplates.values());
    }
    
    void addDirtyImage(ImageEntity image) {
        dirtyImages.put(image.getId(), image);
    }
    
    public List<ImageEntity> getDirtyImages() {
        return new ArrayList<ImageEntity>(dirtyImages.values());
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
    public void setName(String name) {
        getObjectSegment().setName(name);
        markAsDirty();
    }

    @Override
    public Date getCreationTime() {
        return getObjectSegment().getCreationTime();
    }

    @Override
    public AmazonS3Bucket getAmazonS3Bucket() {
        return getObjectSegment().getAmazonS3Bucket();
    }

    @Override
    public void setAmazonS3Bucket(AmazonS3Bucket amazonS3Bucket) {
        getObjectSegment().setAmazonS3Bucket(amazonS3Bucket);
        markAsDirty();
    }

    @Override
    public Template createTemplate(String name, ResizingConfig resizingConfig) {
        TemplateEntity entity = templateFactory.generateTemplate(this, name, resizingConfig);
        addDirtyTemplate(entity);
        return entity;
    }

    @Override
    public void updateTemplate(Template template) {
        checkForInvalidTemplate(template);
        TemplateEntity entity = (TemplateEntity) template;
        addDirtyTemplate(entity);
    }

    @Override
    public void removeTemplate(Template template) {
        checkForInvalidTemplate(template);
        TemplateEntity entity = (TemplateEntity) template;
        if (!entity.isRemovable()) {
            throw new UnremovableTemplateException(entity.getObjectSegment().getId());
        }
        entity.markAsVoid();
        addDirtyTemplate(entity);
    }
    
    private void checkForInvalidTemplate(Template template) {
        if (this != template.getImagePlant()) {
            throw new IllegalArgumentException(template.getId());
        }
    }

    @Override
    public Template fetchTemplateById(String id) {
        return templateRepository.findTemplateById(this, id);
    }

    @Override
    public PaginatedResult<List<Template>> listAllTemplates() {
        return templateRepository.findAllTemplatesByImagePlant(this);
    }

    @Override
    public PaginatedResult<List<Template>> listActiveTemplates() {
        return templateRepository.findActiveTemplatesByImagePlant(this);
    }

    @Override
    public PaginatedResult<List<Template>> listArchivedTemplates() {
        return templateRepository.findArchivedTemplatesByImagePlant(this);
    }

    @Override
    public Image createImage(File imageFile) {
        ImageEntity entity = imageFactory.generateImage(
                this, imageFile, imageRepository, versionRepository, templateRepository);
        addDirtyImage(entity);
        return entity;
    }
    
    public ImageEntity createImage(Image image, Template template) {
        TemplateEntity templateEntity = (TemplateEntity) template;
        ImageEntity entity = imageFactory.generateImage(
                this, (ImageEntity) image, templateEntity, imageRepository, versionRepository, templateRepository);
        addDirtyImage(entity);
        if (!templateEntity.isRemovable()) {
            templateEntity.setNotRemovable();
            dirtyTemplates.put(templateEntity.getId(), templateEntity);
        }
        return entity;
    }

    @Override
    public Image fetchImageById(String id) {
        return imageRepository.findImageById(this, id);
    }

    @Override
    public PaginatedResult<List<Image>> listAllImages() {
        return imageRepository.findAllImages(this);
    }

    @Override
    public void removeImage(Image image) {
        ImageEntity entity = (ImageEntity) image;
        entity.markAsVoid();
        dirtyImages.put(entity.getId(), entity);
    }

    @Override
    public void removeImageAndVerions(Image image) {
        ImageEntity entity = (ImageEntity) image;
        PaginatedResult<List<Version>> result = 
                versionRepository.findVersionsByImage((ImageEntity) image, imageRepository);
        Object pageCursor = result.getNextPageCursor();
        while (null != pageCursor) {
            List<Version> versions = result.getResult(pageCursor);
            for (Version ver: versions) {
                ImageEntity imgEntity = (ImageEntity) ver.getImage();
                dirtyImages.put(imgEntity.getId(), imgEntity);
            }
            pageCursor = result.getNextPageCursor(); //next page.
        }
        dirtyImages.put(entity.getId(), entity);
    }

}
