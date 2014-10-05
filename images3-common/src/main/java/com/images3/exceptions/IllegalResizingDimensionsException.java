package com.images3.exceptions;

import com.images3.common.ResizingUnit;

public class IllegalResizingDimensionsException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9093407154876964467L;
    
    private int width;
    private int height;
    private ResizingUnit unit;
    
    public IllegalResizingDimensionsException(int width, int height,
            ResizingUnit unit, String msg) {
        super(msg);
        this.width = width;
        this.height = height;
        this.unit = unit;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ResizingUnit getUnit() {
        return unit;
    }

}

