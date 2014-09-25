package com.images3.common;

public class IllegalTemplateNameLengthException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3471898997268585831L;
    
    private int minLength;
    private int maxLength;
    private String name;
    
    public IllegalTemplateNameLengthException(String name, int minLength, int maxLength) {
        super("Length of " + name + " need to be greater than "+ minLength + " and less than " + maxLength);
        this.name = name;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getName() {
        return name;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }
    
}

