package com.images3.domain;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.images3.common.PaginatedResult;

public interface Image {
    
    public ImagePlant getImagePlant();
    
    public File getContent();
    
    public Date getDateTime();
    
    public Version fetchVersion(Template template);
    
    public PaginatedResult<List<Version>> fetchAllVersions();
    
}
