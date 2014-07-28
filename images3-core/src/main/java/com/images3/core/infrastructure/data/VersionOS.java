package com.images3.core.infrastructure.data;

public class VersionOS {
    
    private String imageId;
    private String templateId;
    private String vesioningImageId;
    
    public VersionOS(String imageId, String templateId, String vesioningImageId) {
        this.imageId = imageId;
        this.templateId = templateId;
        this.vesioningImageId = vesioningImageId;
    }

    public String getImageId() {
        return imageId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getVesioningImageId() {
        return vesioningImageId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
        result = prime * result
                + ((templateId == null) ? 0 : templateId.hashCode());
        result = prime
                * result
                + ((vesioningImageId == null) ? 0 : vesioningImageId.hashCode());
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
        VersionOS other = (VersionOS) obj;
        if (imageId == null) {
            if (other.imageId != null)
                return false;
        } else if (!imageId.equals(other.imageId))
            return false;
        if (templateId == null) {
            if (other.templateId != null)
                return false;
        } else if (!templateId.equals(other.templateId))
            return false;
        if (vesioningImageId == null) {
            if (other.vesioningImageId != null)
                return false;
        } else if (!vesioningImageId.equals(other.vesioningImageId))
            return false;
        return true;
    }
    
}
