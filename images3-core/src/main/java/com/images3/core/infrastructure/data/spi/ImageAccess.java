package com.images3.core.infrastructure.data.spi;

import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.common.PaginatedResultDelegate;
import com.images3.core.infrastructure.data.ImageOS;
import com.images3.core.infrastructure.data.ImagePlantOS;

public interface ImageAccess extends PaginatedResultDelegate<List<ImageOS>> {
    
    public String generateImageId(ImagePlantOS imagePlant);

    public void insertImage(ImageOS image);
    
    public void deleteImage(ImageOS image);
    
    public ImageOS selectImageById(String imagePlantId, String id);
    
    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(String imagePlantId);
    
}
