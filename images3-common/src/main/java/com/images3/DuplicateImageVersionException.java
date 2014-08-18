package com.images3;

public class DuplicateImageVersionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -2406908279068349252L;
    
    private String templateName;
    private String originalImageId;
    
    public DuplicateImageVersionException(String templateName,
            String originalImageId) {
        super("Template Name: " + templateName 
                + ", Original Image Id: " + originalImageId);
        this.templateName = templateName;
        this.originalImageId = originalImageId;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    public String getOriginalImageId() {
        return originalImageId;
    }
    
}
