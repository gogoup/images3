package com.images3;

public class VersionIdentity {

    private ImageIdentity imageId;
    private String templateId;
    
    public VersionIdentity() {}
    
    public VersionIdentity(ImageIdentity imageId, String templateId) {
        this.imageId = imageId;
        this.templateId = templateId;
    }

    public ImageIdentity getImageId() {
        return imageId;
    }

    public String getTemplateId() {
        return templateId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
        result = prime * result
                + ((templateId == null) ? 0 : templateId.hashCode());
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
        VersionIdentity other = (VersionIdentity) obj;
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
        return true;
    }
    
}
