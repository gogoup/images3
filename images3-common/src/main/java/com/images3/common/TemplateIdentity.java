package com.images3.common;

public class TemplateIdentity {

    private String imagePlantId;
    private String templateName;
    
    public TemplateIdentity() {}
    
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((imagePlantId == null) ? 0 : imagePlantId.hashCode());
        result = prime * result
                + ((templateName == null) ? 0 : templateName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TemplateIdentity other = (TemplateIdentity) obj;
        if (imagePlantId == null) {
            if (other.imagePlantId != null)
                return false;
        } else if (!imagePlantId.equals(other.imagePlantId))
            return false;
        if (templateName == null) {
            if (other.templateName != null)
                return false;
        } else if (!templateName.equals(other.templateName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TemplateIdentity [imagePlantId=" + imagePlantId
                + ", templateName=" + templateName + "]";
    }

    public String getIdentity() {
        return imagePlantId + "-" + templateName;
    }
    
}
