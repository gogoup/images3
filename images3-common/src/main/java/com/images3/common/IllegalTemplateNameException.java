package com.images3.common;

public class IllegalTemplateNameException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2354488093786103075L;
    
    private String name;
    private String pattern;
    
    public IllegalTemplateNameException(String name, String pattern) {
        super("Template name need to be  " + pattern);
        this.name = name;
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public String getPattern() {
        return pattern;
    }
    
}

