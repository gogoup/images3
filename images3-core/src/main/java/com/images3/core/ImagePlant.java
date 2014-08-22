package com.images3.core;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.images3.AmazonS3Bucket;
import com.images3.ResizingConfig;
import org.gogoup.dddutils.pagination.PaginatedResult;

public interface ImagePlant {
    
    public String getId();
    
    public String getName();
    
    public void updateName(String name);
    
    public Date getCreationTime();
    
    public AmazonS3Bucket getAmazonS3Bucket();
    
    public void setAmazonS3Bucket(AmazonS3Bucket amazonS3Bucket);
    
    public Template getMasterTemplate();
    
    public Template createTemplate(String name, ResizingConfig resizingConfig);
    
    public void updateTemplate(Template template);
    
    public Template fetchTemplate(String name);
    
    public PaginatedResult<List<Template>> listAllTemplates();
    
    public PaginatedResult<List<Template>> listActiveTemplates();
    
    public PaginatedResult<List<Template>> listArchivedTemplates();
    
    public void removeTemplate(Template template);
    
    public Image createImage(File imageFile);
    
    public Image createImage(Version version);
    
    public void removeImage(Image image);
    
    public void removeImageAndVerions(Image image);
    
    public Image fetchImageById(String id);
    
    public boolean hasVersiongImage(Version version);
    
    public Image fetchImageByVersion(Version version);
    
    public PaginatedResult<List<Image>> fetchVersioningImages(Image originalImage);
    
    public PaginatedResult<List<Image>> fetchImagesByTemplate(Template template);
    
    public PaginatedResult<List<Image>> listAllImages();
    
}
