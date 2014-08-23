package com.images3.common;

public enum ImageFormat {

    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    BMP("bmp");
    
    private String name;

    private ImageFormat(String name) {
            this.name = name;
    }
    
    public String toString() {
        return name;
    }
}
