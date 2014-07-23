package com.images3.core;

import java.util.Date;
import java.util.List;

import com.images3.common.PaginatedResult;

public interface ImagePlant {

    public String getId();
    
    public String getName();
    
    public Date getCreationTime();
    
    public Template createTemplate(String name, ImageSize scalingSize, boolean isKeepProportions);
    
    public Template fetchTemplateById(String id);
    
    public PaginatedResult<List<Template>> listAllTemplates();
    
    public void removeTemplate(Template template);
    
    public Image createImage(String base64Image);
    
    public Image createImage(Image image, Template template);
    
    public Image fetchImageById(String id);
    
    public void removeImage(Image image);
    
    public void removeImageAndVersions(Image image);
    
    public PaginatedResult<List<Image>> listAllImages();
    
}
