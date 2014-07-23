package com.images3.core;

public interface Template {
    
    public ImagePlant getImagePlant();

    public String getId();
    
    public String getName();
    
    public ImageSize getScalingSize();
    
    public boolean isKeepProportions();
    
    public boolean isArchived();
    
    public boolean isRemovable();
    
    public ImageSize calculateSize(ImageSize size);
    
}
