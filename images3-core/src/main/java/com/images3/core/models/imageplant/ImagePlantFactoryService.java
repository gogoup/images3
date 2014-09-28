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
    private ImageRepositoryService imageRepository;
    private ImageReporterFactoryService imageReporterFactory;
    private TemplateRepositoryService templateRepository;
    
    public ImagePlantFactoryService(ImagePlantAccess imagePlantAccess,
            TemplateFactoryService templateFactory,
            TemplateRepositoryService templateRepository,
            ImageFactoryService imageFactory,
            ImageRepositoryService imageRepository,
            ImageReporterFactoryService imageReporterFactory) {
        this.imagePlantAccess = imagePlantAccess;
        this.templateFactory = templateFactory;
        this.imageFactory = imageFactory;
        this.imageRepository = imageRepository;
        this.imageReporterFactory = imageReporterFactory;
        this.templateRepository = templateRepository;
    }

    @Override
    public ImagePlant generateImagePlant(String name, 
            AmazonS3Bucket amazonS3Bucket, ResizingConfig resizingConfig) {
        String id = imagePlantAccess.genertateImagePlantId();
        Date creationTime = new Date(System.currentTimeMillis());
        long numberOfTemplates = 0;
        ImagePlantOS objectSegment =
                new ImagePlantOS(id, "", creationTime, null,
                        TemplateEntity.MASTER_TEMPLATE_NAME, numberOfTemplates);
        ImagePlantRoot root = reconstituteImagePlant(objectSegment, imageRepository, templateRepository);
        root.markAsNew();
        root.updateName(name);
        root.createMasterTemplate(resizingConfig);
        root.setAmazonS3Bucket(amazonS3Bucket);
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