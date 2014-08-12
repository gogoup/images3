package com.images3.core.models.imageplant;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.utility.DirtyMark;
import org.gogoup.dddutils.pagination.PaginatedResult;

public class ImageEntity extends DirtyMark implements Image {
    
    private ImagePlantRoot imagePlant;
    private ImageOS objectSegment;
    private File imageContent;
    private ImageRepositoryService imageRepository;
    private VersionFactoryService versionFactory;
    private VersionRepositoryService versionRepository;
    private TemplateRepositoryService templateRepository;
    private Map<String, VersionEntity> dirtyVersions;
    
    public ImageEntity(ImagePlantRoot imagePlant, ImageOS objectSegment,
            File imageContent, ImageRepositoryService imageRepository,
            VersionFactoryService versionFactory, VersionRepositoryService versionRepository,
            TemplateRepositoryService templateRepository) {
        this.imagePlant = imagePlant;
        this.objectSegment = objectSegment;
        this.imageContent = imageContent;
        this.imageRepository = imageRepository;
        this.versionFactory = versionFactory;
        this.versionRepository = versionRepository;
        this.templateRepository = templateRepository;
        this.dirtyVersions = new HashMap<String, VersionEntity>();
    }
    
    public ImageOS getObjectSegment() {
        return objectSegment;
    }
    
    public List<VersionEntity> getDirtyVersions() {
        return new ArrayList<VersionEntity>(dirtyVersions.values()); 
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
    public Version createVersion(Template template) {
        ImageEntity versioningImage = imagePlant.createImage(this, template);
        VersionEntity version = versionFactory.generateVersion(
                this, template, versioningImage, templateRepository, imageRepository);
        dirtyVersions.put(template.getId(), version);
        markAsDirty();
        imagePlant.addDirtyImage(this);
        return version;
    }

    @Override
    public Version fetchVersion(Template template) {
        return versionRepository.findVersionById(this, (TemplateEntity) template);
    }

    @Override
    public PaginatedResult<List<Version>> fetchAllVersions() {
        return versionRepository.findVersionsByImage(this);
    }

}
