package com.images3.core.infrastructure.spi;

import java.util.List;

import com.images3.core.infrastructure.ImagePlantOS;
import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public interface ImagePlantAccess extends PaginatedResultDelegate<List<ImagePlantOS>> {

    public String genertateImagePlantId();
    
    public boolean isDuplicatedImagePlantName(String name);
    
    public void insertImagePlant(ImagePlantOS imagePlant);
    
    public void updateImagePlant(ImagePlantOS imagePlant);
    
    public void deleteImagePlant(ImagePlantOS imagePlant);
    
    public ImagePlantOS selectImagePlantById(String id);
    
    public PaginatedResult<List<ImagePlantOS>> selectAllImagePlants();
    
}
