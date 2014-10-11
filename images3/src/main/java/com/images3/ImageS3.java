package com.images3;

import java.io.File;
import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

import com.images3.common.ImageIdentity;
import com.images3.common.TemplateIdentity;

public interface ImageS3 {
    
    public ImagePlantResponse addImagePlant(ImagePlantAddRequest request);
    
    public ImagePlantResponse updateImagePlant(ImagePlantUpdateRequest request);
    
    public void deleteImagePlant(String id);
    
    public ImagePlantResponse getImagePlant(String id);
    
    public PaginatedResult<List<ImagePlantResponse>> getAllImagePlants();
    
    public TemplateResponse addTemplate(TemplateAddRequest request);
    
    public TemplateResponse archiveTemplate(TemplateIdentity id, boolean isArchived);
    
    public void deleteTemplate(TemplateIdentity id);
   
    public TemplateResponse getTemplate(TemplateIdentity id);
    
    public PaginatedResult<List<TemplateResponse>> getAllTemplates(String imagePlantId);
    
    public PaginatedResult<List<TemplateResponse>> getActiveTempaltes(String imagePlantId);
    
    public PaginatedResult<List<TemplateResponse>> getArchivedTemplates(String imagePlantId);
    
    public ImageResponse addImage(ImageAddRequest request);
    
    public void deleteImage(ImageIdentity id);
    
    public ImageResponse getImage(ImageIdentity id);
    
    public ImageResponse getImage(ImageIdentity id, String templateName);
    
    public PaginatedResult<List<ImageResponse>> getImages(String imagePlantId);
    
    public PaginatedResult<List<ImageResponse>> getImages(TemplateIdentity id);

    public PaginatedResult<List<ImageResponse>> getVersioningImages(ImageIdentity originalImageId);

    public File getImageContent(ImageIdentity id);
    
    public File getImageContent(ImageIdentity id, String templateName);
    
    public ImageReportResponse getImageReport(ImageReportQueryRequest request);
    
}
