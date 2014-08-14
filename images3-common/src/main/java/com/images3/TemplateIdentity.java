package com.images3;

public class TemplateIdentity {

    private String imagePlantId;
    private String templateId;
    
    public TemplateIdentity() {}
    
    public TemplateIdentity(String imagePlantId, String templateId) {
        this.imagePlantId = imagePlantId;
        this.templateId = templateId;
    }
    public String getImagePlantId() {
        return imagePlantId;
    }
    public String getTemplateId() {
        return templateId;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((templateId == null) ? 0 : templateId.hashCode());
        result = prime * result
                + ((imagePlantId == null) ? 0 : imagePlantId.hashCode());
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
        if (templateId == null) {
            if (other.templateId != null)
                return false;
        } else if (!templateId.equals(other.templateId))
            return false;
        if (imagePlantId == null) {
            if (other.imagePlantId != null)
                return false;
        } else if (!imagePlantId.equals(other.imagePlantId))
            return false;
        return true;
    }
    
    public String getIdentity() {
        return imagePlantId + "-" + templateId;
    }
    
}
