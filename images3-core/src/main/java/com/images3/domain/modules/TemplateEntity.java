package com.images3.domain.modules;

import com.images3.common.DirtyMark;
import com.images3.domain.ImagePlant;
import com.images3.domain.ImageSize;
import com.images3.domain.Template;
import com.images3.infrastructure.data.TemplateOS;

public class TemplateEntity extends DirtyMark implements Template {

    private ImagePlantRoot imagePlant;
    private TemplateOS objectSegment;
    private ImageSize scalingSize;
    
    public TemplateEntity(ImagePlantRoot imagePlant, TemplateOS objectSegment,
            ImageSize scalingSize) {
        this.imagePlant = imagePlant;
        this.objectSegment = objectSegment;
        this.scalingSize = scalingSize;
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
    public ImageSize getScalingSize() {
        return scalingSize;
    }

    @Override
    public boolean isKeepProportions() {
        return getObjectSegment().isKeepProportions();
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
    public ImageSize calculateSize(ImageSize size) {
        // TODO Auto-generated method stub
        return null;
    }

}
