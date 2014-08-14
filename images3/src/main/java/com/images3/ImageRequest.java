package com.images3;

import java.io.File;

public class ImageRequest {

    private String imagePlantId;
    private File content;
    
    public ImageRequest() {}
    
    public ImageRequest(String imagePlantId, File content) {
        this.imagePlantId = imagePlantId;
        this.content = content;
    }
    
    public String getImagePlantId() {
        return imagePlantId;
    }
    public File getContent() {
        return content;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
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
        ImageRequest other = (ImageRequest) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
        if (imagePlantId == null) {
            if (other.imagePlantId != null)
                return false;
        } else if (!imagePlantId.equals(other.imagePlantId))
            return false;
        return true;
    }

    
    
}
