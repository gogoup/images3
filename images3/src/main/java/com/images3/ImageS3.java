package com.images3;

import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

public interface ImageS3 {
    
    public ImagePlantResponse addImagePlant(ImagePlantCreateRequest request);
    
    public ImagePlantResponse updateImagePlant(ImagePlantUpdateRequest request);
    
    public void deleteImagePlant(String id);
    
    public ImagePlantResponse getImagePlant(String id);
    
    public PaginatedResult<List<ImagePlantResponse>> getAllImagePlants();
    
    public TemplateResponse addTemplate(TemplateCreateRequest request);
    
    public TemplateResponse updateTemplate(TemplateUpdateRequest request);
    
    public void deleteTemplate(TemplateIdentity id);
   
    public TemplateResponse getTemplate(TemplateIdentity id);
    
    public PaginatedResult<List<TemplateResponse>> getAllTemplates(String imagePlantId);
    
    public PaginatedResult<List<TemplateResponse>> getActiveTempaltes(String imagePlantId);
    
    public PaginatedResult<List<TemplateResponse>> getArchivedTemplates(String imagePlantId);
    
    public ImageResponse addImage(ImageRequest request);
    
    public void deleteImage(ImageIdentity id);
    
    public void deleteImageAndVersions(ImageIdentity id);
    
    public ImageResponse getImage(ImageIdentity id);
    
    public ImageResponse getImage(ImageIdentity originalImageId, String templateName);
    
    public PaginatedResult<List<ImageResponse>> getImages(String imagePlantId);
    
    public PaginatedResult<List<ImageResponse>> getVersioningImages(ImageIdentity originalImageId);
    
}
