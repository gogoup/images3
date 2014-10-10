package com.images3.exceptions;

public class DuplicateTemplateNameException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5478836057659298515L;

    private String name;
    
    public DuplicateTemplateNameException(String name, String message) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
}
