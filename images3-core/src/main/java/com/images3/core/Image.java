package com.images3.core;

import java.io.File;
import java.util.Date;

import com.images3.common.ImageMetadata;

public interface Image {
    
    public ImagePlant getImagePlant();
    
    public String getId();
    
    public File getContent();
    
    public Date getDateTime();
    
    public Version getVersion();
    
    public ImageMetadata getMetadata();
    
}
