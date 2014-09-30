package com.images3.common;

public class TemplateIdentity {

    private String imagePlantId;
    private String templateName;
    
    public TemplateIdentity(String imagePlantId, String templateName) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
    }
    
    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getTemplateName() {
        return templateName;
    }

}
