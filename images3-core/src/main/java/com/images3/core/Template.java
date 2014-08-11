package com.images3.core;

import com.images3.ResizingConfig;

public interface Template {
    
    public ImagePlant getImagePlant();

    public String getId();
    
    public void setName(String name);
    
    public String getName();
    
    public boolean isArchived();
    
    public void setArchived(boolean isArchived);
    
    public boolean isRemovable();
    
    public ResizingConfig getResizingConfig();
    
}
