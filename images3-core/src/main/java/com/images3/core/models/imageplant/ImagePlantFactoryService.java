package com.images3.core.models.imageplant;

import java.util.Date;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ResizingConfig;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantFactory;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;

public class ImagePlantFactoryService implements ImagePlantFactory {
    
    private ImagePlantAccess imagePlantAccess;
    private TemplateFactoryService templateFactory;
    private ImageFactoryService imageFactory;
    private ImageReporterFactoryService imageReporterFactory;
    
    public ImagePlantFactoryService(ImagePlantAccess imagePlantAccess,
            TemplateFactoryService templateFactory,
            ImageFactoryService imageFactory,
            ImageReporterFactoryService imageReporterFactory) {
        this.imagePlantAccess = imagePlantAccess;
        this.templateFactory = templateFactory;
        this.imageFactory = imageFactory;
        this.imageReporterFactory = imageReporterFactory;
    }

    @Override
    public ImagePlant generateImagePlant(String name, 
            AmazonS3Bucket amazonS3Bucket, ResizingConfig resizingConfig) {
        String id = imagePlantAccess.genertateImagePlantId();
        Date creationTime = new Date(System.currentTimeMillis());
        long numberOfTemplates = 0;
        ImagePlantOS objectSegment =
                new ImagePlantOS(id, "", creationTime, amazonS3Bucket,
                        TemplateEntity.MASTER_TEMPLATE_NAME, numberOfTemplates);
        ImagePlantRoot root = reconstituteImagePlant(objectSegment, null, null);
        root.markAsNew();
        root.updateName(name);
        root.createMasterTemplate(resizingConfig);
        return root;
    }
    
    public ImagePlantRoot reconstituteImagePlant(ImagePlantOS objectSegment, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        if (null == objectSegment) {
            return null;
        }
        return new ImagePlantRoot(objectSegment,imagePlantAccess, imageFactory, 
                imageRepository, templateFactory, templateRepository, imageReporterFactory);
    }

}