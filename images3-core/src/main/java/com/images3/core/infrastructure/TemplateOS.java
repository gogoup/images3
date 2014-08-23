package com.images3.core.infrastructure;

import com.images3.common.ResizingConfig;
import com.images3.common.TemplateIdentity;

public class TemplateOS {

    private TemplateIdentity id;
    private boolean isArchived;
    private boolean isRemovable;
    private ResizingConfig resizingConfig;
    
    public TemplateOS(TemplateIdentity id, boolean isArchived,
            boolean isRemovable, ResizingConfig resizingConfig) {
        this.id = id;
        this.isArchived = isArchived;
        this.isRemovable = isRemovable;
        this.resizingConfig = resizingConfig;
    }
    
    public boolean isArchived() {
        return isArchived;
    }
    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }
    public boolean isRemovable() {
        return isRemovable;
    }
    public void setRemovable(boolean isRemovable) {
        this.isRemovable = isRemovable;
    }
    public TemplateIdentity getId() {
        return id;
    }
    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }
    
}
