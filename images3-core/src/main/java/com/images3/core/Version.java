package com.images3.core;

public class Version {
    
    private Template template;
    private Image originalImage;
    
    public Version(Template template, Image originalImage) {
        this.template = template;
        this.originalImage = originalImage;
    }

    public Template getTemplate() {
        return template;
    }

    public Image getOriginalImage() {
        return originalImage;
    }
    
    public boolean isMaster() {
        return (template.getName().equalsIgnoreCase(Template.MASTER_TEMPLATE_NAME) 
                && null == originalImage);
    }
    
}
