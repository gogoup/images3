package com.images3.core.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.images3.common.DirtyMark;
import com.images3.common.PaginatedResult;
import com.images3.common.ResizingConfig;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.UserAccount;
import com.images3.core.UserAccountRepository;
import com.images3.core.Version;
import com.images3.core.infrastructure.data.ImagePlantOS;

public class ImagePlantRoot extends DirtyMark implements ImagePlant {
    
    private ImagePlantOS objectSegment;
    private ImageFactoryService imageFactory;
    private ImageRepositoryService imageRepository;
    private TemplateFactoryService templateFactory;
    private TemplateRepositoryService templateRepository;
    private VersionRepositoryService versionRepository;
    private Map<String, TemplateEntity> dirtyTemplates;
    private Map<String, ImageEntity> dirtyImages;
    private UserAccount userAccount;
    private UserAccountRepository userAccountRepository;
    
    public ImagePlantRoot() {}
    
    public ImagePlantRoot(ImagePlantOS objectSegment, ImageFactoryService imageFactory,
            ImageRepositoryService imageRepository, TemplateFactoryService templateFactory,
            TemplateRepositoryService templateRepository, VersionRepositoryService versionRepository,
            UserAccount userAccount, UserAccountRepository userAccountRepository) {
        this.objectSegment = objectSegment;
        this.imageFactory = imageFactory;
        this.imageRepository = imageRepository;
        this.templateFactory = templateFactory;
        this.templateRepository = templateRepository;
        this.versionRepository = versionRepository;
        this.dirtyTemplates = new HashMap<String, TemplateEntity>();
        this.dirtyImages = new HashMap<String, ImageEntity>();
        this.userAccount = userAccount;
        this.userAccountRepository = userAccountRepository;
    }
    
    public ImagePlantOS getObjectSegment() {
        return objectSegment;
    }
    
    public List<TemplateEntity> getDirtyTemplates() {
        return new ArrayList<TemplateEntity>(dirtyTemplates.values());
    }
    
    public List<ImageEntity> getDirtyImages() {
        return new ArrayList<ImageEntity>(dirtyImages.values());
    }

    @Override
    public UserAccount getUserAccount() {
        if (null == userAccount) {
            userAccount = userAccountRepository.findUserAccountById(getObjectSegment().getUserAccountId());
        }
        return userAccount;
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
    public Template createTemplate(String name, ResizingConfig resizingConfig) {
        TemplateEntity entity = templateFactory.generateTemplate(this, name, resizingConfig);
        dirtyTemplates.put(entity.getName(), entity);
        return entity;
    }

    @Override
    public void updateTemplate(Template template) {
        TemplateEntity entity = (TemplateEntity) template;
        dirtyTemplates.put(entity.getName(), entity);
    }

    @Override
    public void removeTemplate(Template template) {
        TemplateEntity entity = (TemplateEntity) template;
        entity.markAsVoid();
        dirtyTemplates.put(entity.getName(), entity);
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
    public Image createImage(String base64Image) {
        ImageEntity entity = imageFactory.generateImage(this, base64Image);
        dirtyImages.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Image createImage(Image image, Template template) {
        TemplateEntity templateEntity = (TemplateEntity) template;
        ImageEntity entity = imageFactory.generateImage(this, image, templateEntity);
        dirtyImages.put(entity.getId(), entity);
        templateEntity.setNotRemovable();
        dirtyTemplates.put(templateEntity.getId(), templateEntity);
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
                versionRepository.findVersionsByImage((ImageEntity) image);
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
