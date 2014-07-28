package com.images3.core.infrastructure.data;

import com.images3.common.ResizingConfig;

public class TemplateOS {

    private String imagePlantId;
    private String id;
    private String name;
    private boolean isArchived;
    private boolean isRemovable;
    private ResizingConfig resizingConfig;
    
    public TemplateOS(String imagePlantId, String id, String name, 
            boolean isArchived, boolean isRemovable, ResizingConfig resizingConfig) {
        this.imagePlantId = imagePlantId;
        this.id = id;
        this.name = name;
        this.isArchived = isArchived;
        this.isRemovable = isRemovable;
        this.resizingConfig = resizingConfig;
    }

    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }
    
}
