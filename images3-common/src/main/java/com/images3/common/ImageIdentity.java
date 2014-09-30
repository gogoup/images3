package com.images3.common;

public class ImageIdentity {

    private String imagePlantId;
    private String imageId;
    
    public ImageIdentity(String imagePlantId, String imageId) {
        this.imagePlantId = imagePlantId;
        this.imageId = imageId;
    }
    public String getImagePlantId() {
        return imagePlantId;
    }
    public String getImageId() {
        return imageId;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imageId == null) ? 0 : imageId.hashCode());
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
        ImageIdentity other = (ImageIdentity) obj;
        if (imageId == null) {
            if (other.imageId != null)
                return false;
        } else if (!imageId.equals(other.imageId))
            return false;
        if (imagePlantId == null) {
            if (other.imagePlantId != null)
                return false;
        } else if (!imagePlantId.equals(other.imagePlantId))
            return false;
        return true;
    }
    
}
