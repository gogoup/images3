package com.images3.core.models.imageplant;

import com.images3.common.DirtyMark;
import com.images3.common.ResizingConfig;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.infrastructure.TemplateOS;

public class TemplateEntity extends DirtyMark implements Template {


    public final static String MASTER_TEMPLATE_NAME = "Master";
    
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
    public String getName() {
        return getObjectSegment().getId().getTemplateName();
    }

    @Override
    public boolean isArchived() {
        return getObjectSegment().isArchived();
    }

    @Override
    public void setArchived(boolean isArchived) {
        getObjectSegment().setArchived(isArchived);
        markAsDirty();
    }

    @Override
    public boolean isRemovable() {
        return getObjectSegment().isRemovable();
    }
    
    public void setNotRemovable() {
        getObjectSegment().setRemovable(false);
        markAsDirty();
    }

    @Override
    public ResizingConfig getResizingConfig() {
        return getObjectSegment().getResizingConfig();
    }

}
