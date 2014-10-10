package com.images3.exceptions;

public class UnsupportedImageFormatException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3707443903382176068L;
    
    public UnsupportedImageFormatException(String message) {
        super(message);
    }

}
