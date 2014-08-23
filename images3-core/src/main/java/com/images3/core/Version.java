package com.images3.core;

public class Version {
    
    private Template template;
    private Image originalImage;
    
    public Version(Template template, Image originalImage) {
        this.template = template;
        this.originalImage = originalImage;
        if (null != originalImage) {
            checkForCurrentVersion();
        }
    }

    public Template getTemplate() {
        return template;
    }

    public Image getOriginalImage() {
        return originalImage;
    }
    
    private void checkForCurrentVersion() {
        if (isCurrentVersion()) {
            throw new IllegalArgumentException(
                    "Create the same version of " + template.getName() + " for image {"
                            + originalImage.getId() + "} is not allowed.");
        }
    }

    private boolean isCurrentVersion() {
        Template originalTemplate = originalImage.getVersion().getTemplate();
        return  (originalTemplate.getName().equalsIgnoreCase(template.getName()));
    }
    
}
