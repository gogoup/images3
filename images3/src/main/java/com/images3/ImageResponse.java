package com.images3;

import java.util.List;

public class ImageResponse {

    private SimpleImageResponse image;
    private List<String> templateNames;
    
    public ImageResponse() {}

    public ImageResponse(SimpleImageResponse image, List<String> templateNames) {
        this.image = image;
        this.templateNames = templateNames;
    }

    public SimpleImageResponse getImage() {
        return image;
    }

    public List<String> getTemplateNames() {
        return templateNames;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        result = prime * result
                + ((templateNames == null) ? 0 : templateNames.hashCode());
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
        ImageResponse other = (ImageResponse) obj;
        if (image == null) {
            if (other.image != null)
                return false;
        } else if (!image.equals(other.image))
            return false;
        if (templateNames == null) {
            if (other.templateNames != null)
                return false;
        } else if (!templateNames.equals(other.templateNames))
            return false;
        return true;
    }
    
}
