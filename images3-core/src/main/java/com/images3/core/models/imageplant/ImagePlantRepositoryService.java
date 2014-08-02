package com.images3.core.models.imageplant;

import java.util.ArrayList;
import java.util.List;

import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantRepository;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.utility.PaginatedResult;
import com.images3.utility.PaginatedResultDelegate;

public class ImagePlantRepositoryService implements ImagePlantRepository, PaginatedResultDelegate<List<ImagePlant>> {
    
    private ImagePlantAccess imagePlantAccess;
    private ImagePlantFactoryService imagePlantFactory;
    private ImageRepositoryService imageRepository;
    private TemplateRepositoryService templateRepository;
    private VersionRepositoryService versionRepository;

    public ImagePlantRepositoryService(ImagePlantAccess imagePlantAccess,
            ImagePlantFactoryService imagePlantFactory,
            ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository,
            VersionRepositoryService versionRepository) {
        this.imagePlantAccess = imagePlantAccess;
        this.imagePlantFactory = imagePlantFactory;
        this.imageRepository = imageRepository;
        this.templateRepository = templateRepository;
        this.versionRepository = versionRepository;
    }

    @Override
    public ImagePlant storeImagePlant(ImagePlant imagePlant) {
        ImagePlantRoot root = (ImagePlantRoot) imagePlant;
        checkIfVoid(root);
        ImagePlantOS objectSegment = root.getObjectSegment();
        if (root.isNew()) {
            imagePlantAccess.insertImagePlant(objectSegment);
            
        } else if (root.isDirty()) {
            imagePlantAccess.updateImagePlant(objectSegment);
        }
        processTemplates(root);
        processImages(root);
        root.cleanMarks();
        return imagePlantFactory.reconstituteImagePlant(
                objectSegment, imageRepository, templateRepository, versionRepository);
    }
    
    private void processTemplates(ImagePlantRoot imagePlant) {
        List<TemplateEntity> templates = imagePlant.getDirtyTemplates();
        for (TemplateEntity template: templates) {
            templateRepository.storeTemplate(template);
        }
    }
    
    private void processImages(ImagePlantRoot imagePlant) {
        List<ImageEntity> images = imagePlant.getDirtyImages();
        for (ImageEntity image: images) {
            imageRepository.storeImage(image);
        }
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
                objectSegment, imageRepository, templateRepository, versionRepository);
    }

    @Override
    public PaginatedResult<List<ImagePlant>> findAllImagePlants() {
        PaginatedResult<List<ImagePlantOS>> osResult = imagePlantAccess.selectAllImagePlants();
        return new PaginatedResult<List<ImagePlant>>(
                this, "getImagePlantsByAccount", new Object[] {osResult}) {};
    }
    
    private List<ImagePlant> getImagePlantsByAccount(PaginatedResult<List<ImagePlantOS>> osResult,
            Object pageCursor) {
        List<ImagePlantOS> objectSegments = osResult.getResult(pageCursor);
        List<ImagePlant> imagePlants = new ArrayList<ImagePlant>(objectSegments.size());
        for (ImagePlantOS os: objectSegments) {
            imagePlants.add(imagePlantFactory.reconstituteImagePlant(
                    os, imageRepository, templateRepository, versionRepository));
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
            PaginatedResult<List<ImagePlantOS>> osResult = (PaginatedResult<List<ImagePlantOS>>) arguments[0];
            return getImagePlantsByAccount(osResult, pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }

    @Override
    public Object getNextPageCursor(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getImagePlantsByAccount".equals(methodName)) {
            PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
            return osResult.getNextPageCursor();
        }
        throw new UnsupportedOperationException(methodName);
    }

}
