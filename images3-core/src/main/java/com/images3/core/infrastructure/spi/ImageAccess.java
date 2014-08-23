package com.images3.core.infrastructure.spi;

import java.util.List;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageVersion;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.ImagePlantOS;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public interface ImageAccess extends PaginatedResultDelegate<List<ImageOS>> {
    
    public boolean isDuplicateVersion(String imagePlantId, ImageVersion version);
    
    public String generateImageId(ImagePlantOS imagePlant);

    public void insertImage(ImageOS image);
    
    public void deleteImage(ImageOS image);
    
    public void deleteImages(String imagePlantId);
    
    public ImageOS selectImageById(ImageIdentity id);
    
    public ImageOS selectImageByVersion(String imagePlantId, ImageVersion version);
    
    public PaginatedResult<List<ImageOS>> selectImagesByOriginalImageId(
            String imagePlantId, String originalImageId);
    
    public PaginatedResult<List<ImageOS>> selectImagesByTemplateName(
            String imagePlantId, String templateName);
    
    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(String imagePlantId);
    
}
