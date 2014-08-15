package com.images3.core.models.imageplant;

import java.util.Date;

import com.images3.AmazonS3Bucket;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantFactory;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;

public class ImagePlantFactoryService implements ImagePlantFactory {
    
    private ImagePlantAccess imagePlantAccess;
    private TemplateFactoryService templateFactory;
    private ImageFactoryService imageFactory;
    
    public ImagePlantFactoryService(ImagePlantAccess imagePlantAccess,
            TemplateFactoryService templateFactory,
            ImageFactoryService imageFactory) {
        this.imagePlantAccess = imagePlantAccess;
        this.templateFactory = templateFactory;
        this.imageFactory = imageFactory;
    }

    @Override
    public ImagePlant generateImagePlant(String name, 
            AmazonS3Bucket amazonS3Bucket) {
        String id = imagePlantAccess.genertateImagePlantId();
        Date creationTime = new Date(System.currentTimeMillis());
        ImagePlantOS objectSegment = new ImagePlantOS(
                id, name, creationTime, amazonS3Bucket);
        ImagePlantRoot root = reconstituteImagePlant(
                objectSegment, null, null, null);
        root.markAsNew();
        root.updateName(name);
        return root;
    }
    
    public ImagePlantRoot reconstituteImagePlant(ImagePlantOS objectSegment, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository,
            VersionRepositoryService versionRepository) {
        if (null == objectSegment) {
            return null;
        }
        return new ImagePlantRoot(objectSegment,imagePlantAccess, imageFactory, 
                imageRepository, templateFactory, templateRepository, versionRepository);
    }

}