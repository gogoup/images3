package com.images3;

public class UnknownImageFormatException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3707443903382176068L;
    
    private String imageId;

    public UnknownImageFormatException(String imageId) {
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }
}
