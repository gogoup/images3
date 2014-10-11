package com.images3.exceptions;

import com.images3.common.ImageVersion;

public class IllegalImageVersioningException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 4465898486919601812L;

    private ImageVersion version;
    
    public IllegalImageVersioningException(ImageVersion version, String message) {
        super(message);
        this.version = version;
    }

    public ImageVersion getVersion() {
        return version;
    }
    
}

