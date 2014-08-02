package com.images3;

public class ImageDimension {

    private int width;
    private int height;
    
    public ImageDimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + width;
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
        ImageDimension other = (ImageDimension) obj;
        if (height != other.height)
            return false;
        if (width != other.width)
            return false;
        return true;
    }
    
}
