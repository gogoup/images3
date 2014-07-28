package com.images3.core.models;

import java.util.ArrayList;
import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.common.PaginatedResultDelegate;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantRepository;
import com.images3.core.UserAccount;
import com.images3.core.UserAccountRepository;
import com.images3.core.infrastructure.data.ImagePlantOS;
import com.images3.core.infrastructure.data.spi.ImagePlantAccess;

public class ImagePlantRepositoryService implements ImagePlantRepository, PaginatedResultDelegate<List<ImagePlant>> {
    
    private ImagePlantAccess imagePlantAccess;
    private ImagePlantFactoryService imagePlantFactory;
    private ImageRepositoryService imageRepository;
    private TemplateRepositoryService templateRepository;
    private VersionRepositoryService versionRepository;
    private UserAccountRepository userAccountRepository;

    public ImagePlantRepositoryService(ImagePlantAccess imagePlantAccess,
            ImagePlantFactoryService imagePlantFactory,
            ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository,
            VersionRepositoryService versionRepository,
            UserAccountRepository userAccountRepository) {
        this.imagePlantAccess = imagePlantAccess;
        this.imagePlantFactory = imagePlantFactory;
        this.imageRepository = imageRepository;
        this.templateRepository = templateRepository;
        this.versionRepository = versionRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public ImagePlant storeImagePlant(ImagePlant imagePlant) {
        ImagePlantRoot root = (ImagePlantRoot) imagePlant;
        checkIfVoid(root);
        ImagePlantOS objectSegment = root.getObjectSegment();
        UserAccount userAccount = null;
        if (root.isNew()) {
            imagePlantAccess.insertImagePlant(objectSegment);
            userAccount = root.getUserAccount();
            
        } else if (root.isDirty()) {
            imagePlantAccess.updateImagePlant(objectSegment);
        }
        root.cleanMarks();
        return imagePlantFactory.reconstituteImagePlant(
                objectSegment, imageRepository, templateRepository, versionRepository, 
                userAccount, userAccountRepository);
    }

    @Override
    public void removeImagePlant(ImagePlant imagePlant) {
        ImagePlantRoot root = (ImagePlantRoot) imagePlant;
        checkIfVoid(root);
        imageRepository.removeImages(root);
        ImagePlantOS objectSegment = root.getObjectSegment();
        imagePlantAccess.deleteImagePlant(objectSegment);
        root.markAsVoid();
    }

    @Override
    public ImagePlant findImagePlantById(String id) {
        ImagePlantOS objectSegment = imagePlantAccess.selectImagePlantById(id);
        return imagePlantFactory.reconstituteImagePlant(
                objectSegment, imageRepository, templateRepository, versionRepository, null, userAccountRepository);
    }

    @Override
    public PaginatedResult<List<ImagePlant>> findImagePlantsByAccount(
            UserAccount userAccount) {
        PaginatedResult<List<ImagePlantOS>> osResult = imagePlantAccess.selectAllImagePlants(userAccount.getId());
        return new PaginatedResult<List<ImagePlant>>(
                this, "getImagePlantsByAccount", new Object[] {userAccount, osResult}) {};
    }
    
    private List<ImagePlant> getImagePlantsByAccount(UserAccount userAccount,
            PaginatedResult<List<ImagePlantOS>> osResult, Object pageCursor) {
        List<ImagePlantOS> objectSegments = osResult.getResult(pageCursor);
        List<ImagePlant> imagePlants = new ArrayList<ImagePlant>(objectSegments.size());
        for (ImagePlantOS os: objectSegments) {
            imagePlants.add(imagePlantFactory.reconstituteImagePlant(
                    os, imageRepository, templateRepository, versionRepository, userAccount, userAccountRepository));
        }
        return imagePlants;
    }
    
    private void checkIfVoid(ImagePlantRoot imagePlant) {
        if (imagePlant.isVoid()) {
            throw new IllegalStateException(imagePlant.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ImagePlant> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getImagePlantsByAccount".equals(methodName)) {
            UserAccount userAccount = (UserAccount) arguments[0];
            PaginatedResult<List<ImagePlantOS>> osResult = (PaginatedResult<List<ImagePlantOS>>) arguments[1];
            return getImagePlantsByAccount(userAccount, osResult, pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }

    @Override
    public Object getNextPageCursor(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getImagePlantsByAccount".equals(methodName)) {
            PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[1];
            return osResult.getNextPageCursor();
        }
        throw new UnsupportedOperationException(methodName);
    }

}
