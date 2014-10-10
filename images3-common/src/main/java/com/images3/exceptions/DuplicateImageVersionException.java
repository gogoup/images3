package com.images3.exceptions;

import com.images3.common.ImageVersion;

public class DuplicateImageVersionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -2406908279068349252L;
    
    private ImageVersion version;
    
    public DuplicateImageVersionException(ImageVersion version, String message) {
        super(message);
        this.version = version;
    }

    public ImageVersion getVersion() {
        return version;
    }
    
}
