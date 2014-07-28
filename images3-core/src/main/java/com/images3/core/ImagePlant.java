package com.images3.core;

import java.util.Date;
import java.util.List;

import com.images3.common.ResizingConfig;
import com.images3.common.PaginatedResult;

public interface ImagePlant {

    public UserAccount getUserAccount();
    
    public String getId();
    
    public String getName();
    
    public void setName(String name);
    
    public Date getCreationTime();
    
    public Template createTemplate(String name, ResizingConfig filterConfig);
    
    public void updateTemplate(Template template);
    
    public Template fetchTemplateById(String id);
    
    public PaginatedResult<List<Template>> listAllTemplates();
    
    public PaginatedResult<List<Template>> listActiveTemplates();
    
    public PaginatedResult<List<Template>> listArchivedTemplates();
    
    public void removeTemplate(Template template);
    
    public Image createImage(String base64Image);
    
    public Image createImage(Image image, Template template);
    
    public Image fetchImageById(String id);
    
    public void removeImage(Image image);
    
    public void removeImageAndVerions(Image image);
    
    public PaginatedResult<List<Image>> listAllImages();
    
}
