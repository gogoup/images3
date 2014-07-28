package com.images3.core.models;

import java.util.Date;

import com.images3.common.DuplicatedImagePlantNameException;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantFactory;
import com.images3.core.UserAccount;
import com.images3.core.UserAccountRepository;
import com.images3.core.infrastructure.data.ImagePlantOS;
import com.images3.core.infrastructure.data.spi.ImagePlantAccess;

public class ImagePlantFactoryService implements ImagePlantFactory {
    
    private ImagePlantAccess imagePlantAccess;
    private TemplateFactoryService templateFactory;
    private ImageFactoryService imageFactory;
    
    public ImagePlantFactoryService(ImagePlantAccess imagePlantAccess,
            TemplateFactoryService templateFactory,
            ImageFactoryService imageFactory) {
        super();
        this.imagePlantAccess = imagePlantAccess;
        this.templateFactory = templateFactory;
        this.imageFactory = imageFactory;
    }

    @Override
    public ImagePlant generateImagePlant(UserAccount account, String name) {
        if (imagePlantAccess.isDuplicatedImagePlantName(account.getId(), name)) {
            throw new DuplicatedImagePlantNameException(name);
        }
        String id = imagePlantAccess.genertateImagePlantId();
        Date creationTime = new Date(System.currentTimeMillis());
        ImagePlantOS objectSegment = new ImagePlantOS(id, name, creationTime, account.getId());
        ImagePlantRoot root = reconstituteImagePlant(objectSegment, null, null, null, account, null);
        root.markAsNew();
        return root;
    }
    
    public ImagePlantRoot reconstituteImagePlant(ImagePlantOS objectSegment, ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository, VersionRepositoryService versionRepository,
            UserAccount userAccount, UserAccountRepository userAccountRepository) {
        if (null == objectSegment) {
            return null;
        }
        return new ImagePlantRoot(objectSegment, imageFactory, imageRepository, templateFactory,
                templateRepository, versionRepository, userAccount, userAccountRepository);
    }

}