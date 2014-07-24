package com.images3.domain;

public interface Template {
    
    public ImagePlant getImagePlant();

    public String getId();
    
    public String getName();
    
    public ImageSize getScalingSize();
    
    public boolean isKeepProportions();
    
    public boolean isArchived();
    
    public void setArchived(boolean isArchived);
    
    public boolean isRemovable();
    
    public void setNotRemovable();
    
    public ImageSize calculateSize(ImageSize size);
    
}
