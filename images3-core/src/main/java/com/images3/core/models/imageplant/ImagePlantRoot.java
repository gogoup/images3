package com.images3.core.models.imageplant;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.DirtyMark;
import com.images3.common.ResizingConfig;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.ImageReporter;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.exceptions.AmazonS3BucketAccessFailedException;
import com.images3.exceptions.DuplicateImagePlantNameException;
import com.images3.exceptions.IllegalImagePlantNameLengthException;
import com.images3.exceptions.UnachievableTemplateException;
import com.images3.exceptions.UnremovableTemplateException;

import org.gogoup.dddutils.pagination.PaginatedResult;

public class ImagePlantRoot extends DirtyMark implements ImagePlant {
    
    private static final int IMAGEPLANT_NAME_MIN_LENGTH = 3;
    private static final int IMAGEPLANT_NAME_MAX_LENGTH = 100;
    
    private ImagePlantOS objectSegment;
    private ImagePlantAccess imagePlantAccess;
    private ImageFactoryService imageFactory;
    private ImageRepositoryService imageRepository;
    private TemplateFactoryService templateFactory;
    private TemplateRepositoryService templateRepository;
    private Map<String, TemplateEntity> dirtyTemplates;
    private Map<String, ImageEntity> dirtyImages;
    private Template masterTemplate;
    private ImageReporterFactoryService imageReporterFactory;
    
    public ImagePlantRoot() {}
    
    public ImagePlantRoot(ImagePlantOS objectSegment, ImagePlantAccess imagePlantAccess, 
            ImageFactoryService imageFactory, ImageRepositoryService imageRepository,
            TemplateFactoryService templateFactory, TemplateRepositoryService templateRepository,
            ImageReporterFactoryService imageReporterFactory) {
        this.objectSegment = objectSegment;
        this.imagePlantAccess = imagePlantAccess;
        this.imageFactory = imageFactory;
        this.imageRepository = imageRepository;
        this.templateFactory = templateFactory;
        this.templateRepository = templateRepository;
        this.dirtyTemplates = new HashMap<String, TemplateEntity>();
        this.dirtyImages = new HashMap<String, ImageEntity>();
        this.masterTemplate = null;
        this.imageReporterFactory = imageReporterFactory;
    }
    
    public ImagePlantOS getObjectSegment() {
        return objectSegment;
    }
    
    private void addNewTemplate(TemplateEntity template) {
        getObjectSegment().setNumberOfTemplates(getObjectSegment().getNumberOfTemplates() + 1);
        this.markAsDirty();
        dirtyTemplates.put(template.getName(), template);
    }
    
    private void addRemovedTemplate(TemplateEntity template) {
        template.markAsVoid();
        getObjectSegment().setNumberOfTemplates(getObjectSegment().getNumberOfTemplates() - 1);
        this.markAsDirty();
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
        validateImagePlantName(name);
        if (getObjectSegment().getName().equals(name)) {
            return;
        }
        checkForDuplicateName(name);
        getObjectSegment().setName(name);
        markAsDirty();
    }
    
    private void validateImagePlantName(String name) {
        if (null == name) {
            throw new NullPointerException("ImagePlant name");
        }
        int length = name.trim().length();
        if (length < IMAGEPLANT_NAME_MIN_LENGTH
                || length > IMAGEPLANT_NAME_MAX_LENGTH) {
            String message = "Use " + IMAGEPLANT_NAME_MIN_LENGTH 
                    + " to " + IMAGEPLANT_NAME_MAX_LENGTH + " characters";
            throw new IllegalImagePlantNameLengthException(
                    name, IMAGEPLANT_NAME_MIN_LENGTH, IMAGEPLANT_NAME_MAX_LENGTH, message);
        }
    }
    
    private void checkForDuplicateName(String name) {
        if (imagePlantAccess.isDuplicatedImagePlantName(name)) {
            String message = "Name \'" + name + "\' has been taken.";
            throw new DuplicateImagePlantNameException(name, message);
        }
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
        checkForAmazonS3BucketAccessibility(amazonS3Bucket);
        getObjectSegment().setAmazonS3Bucket(amazonS3Bucket);
        markAsDirty();
    }
    
    private void checkForAmazonS3BucketAccessibility(AmazonS3Bucket amazonS3Bucket) {
        if (!imageRepository.validateBucket(amazonS3Bucket)) {
            String message = "Failed to access bucket with the combination of api key and secret.";
            throw new AmazonS3BucketAccessFailedException(amazonS3Bucket, message);
        }
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
        addNewTemplate(entity);
        return entity;
    }
    
    public Template createMasterTemplate(ResizingConfig resizingConfig) {
        TemplateEntity entity = templateFactory.generateMasterTemplate(this, resizingConfig);
        addNewTemplate(entity);
        return entity;
    }

    @Override
    public void updateTemplate(Template template) {
        checkForInvalidTemplate(template);
        TemplateEntity entity = (TemplateEntity) template;
        checkForArchivedMasterTemplate(entity);
        dirtyTemplates.put(entity.getName(), entity);
    }
    
    private void checkForArchivedMasterTemplate(TemplateEntity template) {
        if (template.getName().equalsIgnoreCase(getObjectSegment().getMasterTemplateName())
                && template.isArchived()) {
            throw new UnachievableTemplateException(
                    template.getObjectSegment().getId(), 
                    "Archive master template is not allowed!");
        }
    }

    @Override
    public void removeTemplate(Template template) {
        checkForInvalidTemplate(template);
        TemplateEntity entity = (TemplateEntity) template;
        checkForUnremoveTemplate(entity);
        addRemovedTemplate(entity);
    }
    
    private void checkForUnremoveTemplate(TemplateEntity template) {
        if (!template.isRemovable()) {
            String message = "Template " + template.getName() + " cannot be removed.";
            throw new UnremovableTemplateException(template.getObjectSegment().getId(), message);
        }
    }
    
    private void checkForInvalidTemplate(Template template) {
        if (this != template.getImagePlant()) {
            throw new IllegalArgumentException(template.getName());
        }
    }

    @Override
    public Template fetchTemplate(String name) {
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
        ImageEntity entity = imageFactory.generateImage(
                this, version, imageRepository,
                templateRepository);
        addDirtyImage(entity);
        TemplateEntity templateEntity = (TemplateEntity) version.getTemplate();
        if (templateEntity.isRemovable()) {
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
    public PaginatedResult<List<Image>> fetchImagesByTemplate(Template template) {
        return imageRepository.findImagesByTemplate(this, template);
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

    @Override
    public long countTemplates() {
        return getObjectSegment().getNumberOfTemplates();
    }

    @Override
    public ImageReporter generateImageReporter() {
        return imageReporterFactory.generateImageReporter(this);
    }

    @Override
    public ImageReporter generateImageReporter(Template template) {
        checkForInvalidTemplate(template);
        return imageReporterFactory.generateImageReporter(template);
    }

}
