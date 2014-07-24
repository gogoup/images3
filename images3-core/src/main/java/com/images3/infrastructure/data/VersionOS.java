package com.images3.infrastructure.data;

public class VersionOS {
    
    private String imageId;
    private String templateId;
    private String actualImageId;
    
    public VersionOS(String imageId, String templateId, String actualImageId) {
        this.imageId = imageId;
        this.templateId = templateId;
        this.actualImageId = actualImageId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getActualImageId() {
        return actualImageId;
    }
    
}
