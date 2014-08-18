package com.images3;

public class DuplicateTemplateNameException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5478836057659298515L;

    private String name;
    
    public DuplicateTemplateNameException(String name) {
        super(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
}
