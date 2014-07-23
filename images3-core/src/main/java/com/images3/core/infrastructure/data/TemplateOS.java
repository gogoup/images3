package com.images3.core.infrastructure.data;

public class TemplateOS {

    private String imagePlantId;
    private String id;
    private String name;
    private ImageSizeOS scalingSize;
    private boolean isKeepProportions;
    private boolean isArchived;
    private boolean isRemovable;
    
    public TemplateOS(String imagePlantId, String id, String name,
            ImageSizeOS scalingSize, boolean isKeepProportions,
            boolean isArchived, boolean isRemovable) {
        this.imagePlantId = imagePlantId;
        this.id = id;
        this.name = name;
        this.scalingSize = scalingSize;
        this.isKeepProportions = isKeepProportions;
        this.isArchived = isArchived;
        this.isRemovable = isRemovable;
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

    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getId() {
        return id;
    }

    public ImageSizeOS getScalingSize() {
        return scalingSize;
    }

    public boolean isKeepProportions() {
        return isKeepProportions;
    }
    
}
