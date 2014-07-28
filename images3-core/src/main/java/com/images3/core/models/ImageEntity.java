package com.images3.core.models;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.images3.common.DirtyMark;
import com.images3.common.PaginatedResult;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.data.ImageOS;

public class ImageEntity extends DirtyMark implements Image {
    
    private ImagePlantRoot imagePlant;
    private ImageOS objectSegment;
    private VersionRepositoryService versionRepository;
    
    public ImageEntity(ImagePlantRoot imagePlant, ImageOS objectSegment,
            VersionRepositoryService versionRepository) {
        this.imagePlant = imagePlant;
        this.objectSegment = objectSegment;
        this.versionRepository = versionRepository;
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
        return getObjectSegment().getId();
    }

    @Override
    public File getContent() {
        return getObjectSegment().getContent();
    }

    @Override
    public Date getDateTime() {
        return getObjectSegment().getDateTime();
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
