package com.images3.exceptions;

public class IllegalResizingDimensionsException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9093407154876964467L;
    
    private int width;
    private int height;
    
    public IllegalResizingDimensionsException(int width, int height, String msg) {
        super(msg);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}

