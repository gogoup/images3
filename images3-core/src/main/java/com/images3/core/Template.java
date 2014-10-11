package com.images3.core;

import com.images3.common.ResizingConfig;

public interface Template {
    
    public final static String MASTER_TEMPLATE_NAME = "Master";
    
    public ImagePlant getImagePlant();
    
    public String getName();
    
    public boolean isArchived();
    
    public void setArchived(boolean isArchived);
    
    public boolean isRemovable();
    
    public ResizingConfig getResizingConfig();
    
}
