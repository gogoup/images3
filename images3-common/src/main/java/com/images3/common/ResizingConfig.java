package com.images3.common;

public class ResizingConfig {
    
    private ResizingUnit unit;
    private int width;
    private int height;
    private boolean isKeepProportions;
    
    public ResizingConfig(ResizingUnit unit, int width, int height,
            boolean isKeepProportions) {
        this.unit = unit;
        this.width = width;
        this.height = height;
        this.isKeepProportions = isKeepProportions;
    }
    
    public ResizingUnit getUnit() {
        return unit;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isKeepProportions() {
        return isKeepProportions;
    }

   
}
