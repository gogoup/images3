package com.images3.core.infrastructure.spi;

import java.util.List;

import com.images3.ImageIdentity;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.VersionOS;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public interface ImageAccess extends PaginatedResultDelegate<List<ImageOS>> {
    
    public boolean isDuplicateVersion(VersionOS version);
    
    public String generateImageId(ImagePlantOS imagePlant);

    public void insertImage(ImageOS image);
    
    public void deleteImage(ImageOS image);
    
    public ImageOS selectImageById(ImageIdentity id);
    
    public ImageOS selectImageByVersion(VersionOS version);
    
    public PaginatedResult<List<ImageOS>> selectImagesByOriginalImageId(
            String imagePlantId, String originalImageId);
    
    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(String imagePlantId);
    
}
