package com.images3;

import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

public interface ImageS3 {
    
    public ImagePlantResponse addImagePlant(ImagePlantRequest request);
    
    public ImagePlantResponse updateImagePlant(String id, ImagePlantRequest request);
    
    public void deleteImagePlant(String id);
    
    public ImagePlantResponse getImagePlant(String id);
    
    public PaginatedResult<List<ImagePlantResponse>> getAllImagePlants();
    
    public TemplateResponse addTemplate(TemplateRequest request);
    
    public TemplateResponse updateTemplate(TemplateIdentity id, TemplateRequest request);
    
    public void deleteTemplate(TemplateIdentity id);
   
    public TemplateResponse getTemplate(TemplateIdentity id);
    
    public PaginatedResult<List<TemplateResponse>> getActiveTempaltes(String imagePlantId);
    
    public PaginatedResult<List<TemplateResponse>> getArchivedTemplates(String imagePlantId);
    
    public ImageResponse addImage(ImageRequest request);
    
    public void deleteImage(ImageIdentity id);
    
    public void deleteImageAndVersions(ImageIdentity id);
    
    public ImageResponse getImage(ImageIdentity id);
    
    public ImageResponse getVersioningImage(VersionIdentity id);
    
    public PaginatedResult<List<ImageResponse>> getImages(String imagePlantId);
    
    public PaginatedResult<List<ImageResponse>> getVersioningImages(ImageIdentity id);
    
}
