package com.images3.exceptions;

public class DuplicateImagePlantNameException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5493061200459533361L;

    private String name;
    
    public DuplicateImagePlantNameException(String name, String message) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}

