package com.images3.core.models.imageplant;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.images3.AmazonS3Bucket;
import com.images3.DuplicatedImagePlantNameException;
import com.images3.ResizingConfig;
import com.images3.UnremovableTemplateException;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.utility.DirtyMark;

import org.gogoup.dddutils.pagination.PaginatedResult;

public class ImagePlantRoot extends DirtyMark implements ImagePlant {
    
    private ImagePlantOS objectSegment;
    private ImagePlantAccess imagePlantAccess;
    private ImageFactoryService imageFactory;
    private ImageRepositoryService imageRepository;
    private TemplateFactoryService templateFactory;
    private TemplateRepositoryService templateRepository;
    private Map<String, TemplateEntity> dirtyTemplates;
    private Map<String, ImageEntity> dirtyImages;
    private Template masterTemplate;
    
    public ImagePlantRoot() {}
    
    public ImagePlantRoot(ImagePlantOS objectSegment, ImagePlantAccess imagePlantAccess, 
            ImageFactoryService imageFactory, ImageRepositoryService imageRepository,
            TemplateFactoryService templateFactory, TemplateRepositoryService templateRepository) {
        this.objectSegment = objectSegment;
        this.imagePlantAccess = imagePlantAccess;
        this.imageFactory = imageFactory;
        this.imageRepository = imageRepository;
        this.templateFactory = templateFactory;
        this.templateRepository = templateRepository;
        this.dirtyTemplates = new HashMap<String, TemplateEntity>();
        this.dirtyImages = new HashMap<String, ImageEntity>();
        this.masterTemplate = null;
    }
    
    public ImagePlantOS getObjectSegment() {
        return objectSegment;
    }
    
    void addDirtyTemplate(TemplateEntity template) {
        dirtyTemplates.put(template.getName(), template);
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
    public void updateName(String name) {
        if (getObjectSegment().getName().equals(name)) {
            return;
        }
        if (imagePlantAccess.isDuplicatedImagePlantName(name)) {
            throw new DuplicatedImagePlantNameException(name);
        }
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
        if (getObjectSegment().getAmazonS3Bucket().equals(amazonS3Bucket)) {
            return;
        }
        getObjectSegment().setAmazonS3Bucket(amazonS3Bucket);
        markAsDirty();
    }

    @Override
    public Template getMasterTemplate() {
        if (null == masterTemplate) {
            masterTemplate = 
                    templateRepository.findTemplateByName(
                            this, getObjectSegment().getMasterTemplateName());
        }
        return masterTemplate;
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
        checkForArchivedMasterTemplate(template);
        TemplateEntity entity = (TemplateEntity) template;
        addDirtyTemplate(entity);
    }
    
    private void checkForArchivedMasterTemplate(Template template) {
        if (template.getName().equalsIgnoreCase(getObjectSegment().getMasterTemplateName())) {
            throw new UnsupportedOperationException("Modify master template is not allowed!");
        }
    }

    @Override
    public void removeTemplate(Template template) {
        checkForInvalidTemplate(template);
        TemplateEntity entity = (TemplateEntity) template;
        checkForUnremoveTemplate(entity);
        entity.markAsVoid();
        addDirtyTemplate(entity);
    }
    
    private void checkForUnremoveTemplate(TemplateEntity template) {
        if (!template.isRemovable()) {
            throw new UnremovableTemplateException(template.getObjectSegment().getId());
        }
    }
    
    private void checkForInvalidTemplate(Template template) {
        if (this != template.getImagePlant()) {
            throw new IllegalArgumentException(template.getName());
        }
    }

    @Override
    public Template fetchTemplateByName(String name) {
        return templateRepository.findTemplateByName(this, name);
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
                this, imageFile, imageRepository, templateRepository);
        addDirtyImage(entity);
        return entity;
    }
    
    @Override
    public ImageEntity createImage(Version version) {
        TemplateEntity templateEntity = (TemplateEntity) version.getTemplate();
        ImageEntity entity = imageFactory.generateImage(
                this, (ImageEntity) version.getOriginalImage(), templateEntity,
                imageRepository, templateRepository);
        addDirtyImage(entity);
        if (!templateEntity.isRemovable()) {
            templateEntity.setNotRemovable();
            dirtyTemplates.put(templateEntity.getName(), templateEntity);
        }
        return entity;
    }

    @Override
    public Image fetchImageById(String id) {
        return imageRepository.findImageById(this, id);
    }

    @Override
    public boolean hasVersiongImage(Version version) {
        return imageRepository.hasVersioningImage(this, version);
    }

    @Override
    public Image fetchImageByVersion(Version version) {
        return imageRepository.findImageByVersion(this, version);
    }

    @Override
    public PaginatedResult<List<Image>> fetchVersioningImages(
            Image originalImage) {
        return imageRepository.findVersioningImages(this, originalImage);
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
        PaginatedResult<List<Image>> result = fetchVersioningImages(image);
        Object pageCursor = result.getNextPageCursor();
        while (null != pageCursor) {
            List<Image> images = result.getResult(pageCursor);
            for (Image img: images) {
                ImageEntity imgEntity = (ImageEntity) img;
                dirtyImages.put(imgEntity.getId(), imgEntity);
            }
            pageCursor = result.getNextPageCursor(); //next page.
        }
        dirtyImages.put(entity.getId(), entity);
    }

}
