package com.images3.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

public interface Image {
    
    public ImagePlant getImagePlant();
    
    public String getId();
    
    public File getContent();
    
    public Date getDateTime();
    
    public Version createVersion(Template template);
    
    public Version fetchVersion(Template template);
    
    public PaginatedResult<List<Version>> fetchAllVersions();
    
}
