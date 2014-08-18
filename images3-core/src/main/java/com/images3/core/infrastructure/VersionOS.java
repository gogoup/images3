package com.images3.core.infrastructure;

public class VersionOS {
    
    private String templateName;
    private String originalImageId;
    
    public VersionOS(String templateName, String originalImageId) {
        this.templateName = templateName;
        this.originalImageId = originalImageId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getOriginalImageId() {
        return originalImageId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((originalImageId == null) ? 0 : originalImageId.hashCode());
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
        VersionOS other = (VersionOS) obj;
        if (originalImageId == null) {
            if (other.originalImageId != null)
                return false;
        } else if (!originalImageId.equals(other.originalImageId))
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
        return "VersionOS [templateName=" + templateName + ", originalImageId="
                + originalImageId + "]";
    }
    
}
