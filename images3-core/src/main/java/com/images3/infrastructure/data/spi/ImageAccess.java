package com.images3.infrastructure.data.spi;

import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.infrastructure.data.ImageOS;
import com.images3.infrastructure.data.ImagePlantOS;
import com.images3.infrastructure.data.MetadataOS;

public interface ImageAccess {
    
    public String generateImageId(ImagePlantOS imagePlant);

    public void insertImage(ImageOS image, MetadataOS metadata);
    
    public void deleteImage(ImageOS image);
    
    public void deletleImagesByImagePlantId(String imagePlantId);
    
    public ImageOS selectImageById(String imagePlantId, String id);
    
    public PaginatedResult<List<ImageOS>> selectImagesByImagePlantId(String imagePlantId);
    
    public void deleteMetadata(ImageOS image);
    
    public void deleteMetadataByImagePlantId(String imagePlantId);
    
    public MetadataOS selectMetadata(ImageOS image);
    
}
