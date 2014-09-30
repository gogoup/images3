package com.images3;

import com.images3.common.ResizingConfig;
import com.images3.common.TemplateIdentity;

public class TemplateResponse {

    private TemplateIdentity id;
    private boolean isArchived;
    private boolean isRemovable;
    private ResizingConfig resizingConfig;
    
    public TemplateResponse(TemplateIdentity id, boolean isArchived,
            boolean isRemovable, ResizingConfig resizingConfig) {
        this.id = id;
        this.isArchived = isArchived;
        this.isRemovable = isRemovable;
        this.resizingConfig = resizingConfig;
    }

    public TemplateIdentity getId() {
        return id;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }

}
