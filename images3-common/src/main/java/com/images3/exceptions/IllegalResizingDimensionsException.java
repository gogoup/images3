package com.images3.exceptions;

import com.images3.common.ResizingUnit;

public class IllegalResizingDimensionsException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9093407154876964467L;
    
    private int minimum;
    private int maximum;
    private ResizingUnit unit;
    
    public IllegalResizingDimensionsException(int minimum, int maximum,
            ResizingUnit unit, String msg) {
        super(msg);
        this.minimum = minimum;
        this.maximum = maximum;
        this.unit = unit;
    }

    public int getMinimum() {
        return minimum;
    }


    public int getMaximum() {
        return maximum;
    }

    public ResizingUnit getUnit() {
        return unit;
    }

}

