package com.images3.core;

import java.util.List;

import com.images3.common.PaginatedResult;

public interface ImagePlantRepository {

    public ImagePlant storeImagePlant(ImagePlant imagePlant);
    
    public void removeImagePlant(ImagePlant imagePlant);
    
    public ImagePlant findImagePlantById(String id);
    
    public PaginatedResult<List<ImagePlant>> findImagePlantsByAccount(
            UserAccount userAccount);
    
}
