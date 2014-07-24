package com.images3.infrastructure.data.spi;

import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.infrastructure.data.ImagePlantOS;

public interface ImagePlantAccess {

    public String genertateImagePlantId();
    
    public boolean isDuplicatedImagePlantName(String userAccountId, String name);
    
    public void insertImagePlant(ImagePlantOS imagePlant);
    
    public void updateImagePlant(ImagePlantOS imagePlant);
    
    public void deleteImagePlant(ImagePlantOS imagePlant);
    
    public ImagePlantOS selectImagePlantById(String id);
    
    public PaginatedResult<List<ImagePlantOS>> selectAllImagePlants();
    
}
