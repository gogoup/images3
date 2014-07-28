package com.images3.common;

public class ImageMetadata {

    private ImageDimension dimension;
    private String format;
    private int size;
    
    public ImageMetadata(ImageDimension dimension, String format, int size) {
        this.dimension = dimension;
        this.format = format;
        this.size = size;
    }

    public ImageDimension getDimension() {
        return dimension;
    }

    public String getFormat() {
        return format;
    }

    public int getSize() {
        return size;
    }
    
}
