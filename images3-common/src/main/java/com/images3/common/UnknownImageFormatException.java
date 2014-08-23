package com.images3.common;

public class UnknownImageFormatException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3707443903382176068L;
    
    private String imageId;

    public UnknownImageFormatException(String imageId) {
        super(imageId);
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }
}
