package com.images3.domain.modules;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.domain.Image;
import com.images3.domain.ImagePlant;
import com.images3.domain.Template;
import com.images3.domain.Version;
import com.images3.infrastructure.data.ImageOS;

public class ImageEntity implements Image {
    
    private ImageOS objectSegment;
    
    public ImageEntity(ImageOS objectSegment) {
        this.objectSegment = objectSegment;
    }
    
    public ImageOS getObjectSegment() {
        return objectSegment;
    }

    @Override
    public ImagePlant getImagePlant() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public File getContent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Date getDateTime() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Version fetchVersion(Template template) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginatedResult<List<Version>> fetchAllVersions() {
        // TODO Auto-generated method stub
        return null;
    }

}
