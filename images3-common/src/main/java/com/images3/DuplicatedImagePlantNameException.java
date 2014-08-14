package com.images3;

public class DuplicatedImagePlantNameException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5493061200459533361L;

    private String name;
    
    public DuplicatedImagePlantNameException(String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}
