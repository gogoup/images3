package com.images3.infrastructure.data;

public class MetadataOS {

    private int width;
    private int height;
    private String format;
    private int size;
    
    public MetadataOS(int width, int height, String format, int size) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getFormat() {
        return format;
    }

    public int getSize() {
        return size;
    }
    
}
