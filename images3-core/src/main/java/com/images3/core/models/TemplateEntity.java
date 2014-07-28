package com.images3.core.models;

import com.images3.common.DirtyMark;
import com.images3.common.ResizingConfig;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.infrastructure.data.TemplateOS;

public class TemplateEntity extends DirtyMark implements Template {

    private ImagePlantRoot imagePlant;
    private TemplateOS objectSegment;
    
    public TemplateEntity(ImagePlantRoot imagePlant, TemplateOS objectSegment) {
        this.imagePlant = imagePlant;
        this.objectSegment = objectSegment;
    }
    
    public TemplateOS getObjectSegment() {
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
    public String getName() {
        return getObjectSegment().getName();
    }

    @Override
    public boolean isArchived() {
        return getObjectSegment().isArchived();
    }

    @Override
    public void setArchived(boolean isArchived) {
        getObjectSegment().setArchived(isArchived);
    }

    @Override
    public boolean isRemovable() {
        return getObjectSegment().isRemovable();
    }
    
    @Override
    public void setNotRemovable() {
        getObjectSegment().setRemovable(false);
    }

    @Override
    public ResizingConfig getResizingConfig() {
        return getObjectSegment().getResizingConfig();
    }

}
