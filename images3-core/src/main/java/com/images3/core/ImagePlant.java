package com.images3.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.images3.AmazonS3Bucket;
import com.images3.ResizingConfig;
import com.images3.utility.PaginatedResult;

public interface ImagePlant {
    
    public String getId();
    
    public String getName();
    
    public void setName(String name);
    
    public Date getCreationTime();
    
    public AmazonS3Bucket getAmazonS3Bucket();
    
    public void setAmazonS3Bucket(AmazonS3Bucket amazonS3Bucket);
    
    public Template createTemplate(String name, ResizingConfig filterConfig);
    
    public void updateTemplate(Template template);
    
    public Template fetchTemplateById(String id);
    
    public PaginatedResult<List<Template>> listAllTemplates();
    
    public PaginatedResult<List<Template>> listActiveTemplates();
    
    public PaginatedResult<List<Template>> listArchivedTemplates();
    
    public void removeTemplate(Template template);
    
    public Image createImage(File imageFile);
    
    public Image fetchImageById(String id);
    
    public void removeImage(Image image);
    
    public void removeImageAndVerions(Image image);
    
    public PaginatedResult<List<Image>> listAllImages();
    
}
