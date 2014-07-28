package com.images3.core;

import com.images3.common.ResizingConfig;

public interface Template {
    
    public ImagePlant getImagePlant();

    public String getId();
    
    public String getName();
    
    public boolean isArchived();
    
    public void setArchived(boolean isArchived);
    
    public boolean isRemovable();
    
    public ResizingConfig getResizingConfig();
    
}
