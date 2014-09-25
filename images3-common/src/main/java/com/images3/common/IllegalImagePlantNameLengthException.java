package com.images3.common;

public class IllegalImagePlantNameLengthException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -886152892167574162L;
    
    private int minLength;
    private int maxLength;
    private String name;
    
    public IllegalImagePlantNameLengthException(String name, int minLength, int maxLength) {
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

