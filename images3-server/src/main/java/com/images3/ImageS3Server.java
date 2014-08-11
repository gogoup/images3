package com.images3;

import java.util.List;

import org.gogoup.dddutils.pagination.PaginatedResult;

public class ImageS3Server implements ImageS3 {
    
    private ImageS3Server() {
        
    }

    @Override
    public ImagePlantResponse addImagePlant(ImagePlantRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImagePlantResponse updateImagePlant(ImagePlantRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteImagePlant(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public ImagePlantResponse getImagePlant(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginatedResult<List<ImagePlantResponse>> getAllImagePlants() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemplateResponse addTemplate(TemplateRequest reuqest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemplateResponse updateTemplate(TemplateRequest reuqest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemplateResponse archiveTemplate(TemplateIdentity id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteTemplate(TemplateIdentity id) {
        // TODO Auto-generated method stub

    }

    @Override
    public TemplateResponse getTemplate(TemplateIdentity id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginatedResult<List<TemplateResponse>> getActiveTempaltes(
            String imagePlantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginatedResult<List<TemplateResponse>> getArchivedTemplates(
            String imagePlantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImageResponse addImage(ImageRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteImage(ImageIdentity id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteImageAndVersions(ImageIdentity id) {
        // TODO Auto-generated method stub

    }

    @Override
    public ImageResponse getImage(ImageIdentity id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ImageResponse getVersioningImage(VersionIdentity id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginatedResult<List<ImageResponse>> getImages(String imagePlantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaginatedResult<List<ImageResponse>> getVersioningImages(
            ImageIdentity id) {
        // TODO Auto-generated method stub
        return null;
    }

    public static class Builder {
        
        public ImageS3Server build() {
            return new ImageS3Server();
        }
    }
}
