package com.images3;

public class ResizingConfig {
    
    private ResizingUnit unit;
    private int width;
    private int height;
    private boolean isKeepProportions;
    
    public ResizingConfig() {}
    
    public ResizingConfig(ResizingUnit unit, int width, int height,
            boolean isKeepProportions) {
        this.unit = unit;
        this.width = width;
        this.height = height;
        this.isKeepProportions = isKeepProportions;
        checkForPercentResizing();
    }
    
    private void checkForPercentResizing() {
        if (unit == ResizingUnit.PERCENT) {
            if (width <= 0 || width > 100) {
                throw new IllegalArgumentException("Percent of width need to be 1 to 100.");
            }
            if (height <= 0 || height > 100) {
                throw new IllegalArgumentException("Percent of height need to be 1 to 100.");
            }
        }
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + (isKeepProportions ? 1231 : 1237);
        result = prime * result + ((unit == null) ? 0 : unit.hashCode());
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ResizingConfig other = (ResizingConfig) obj;
        if (height != other.height)
            return false;
        if (isKeepProportions != other.isKeepProportions)
            return false;
        if (unit != other.unit)
            return false;
        if (width != other.width)
            return false;
        return true;
    }
    
    
}
