package com.images3.core.models.imageplant;

import java.util.ArrayList;
import java.util.List;

import com.images3.NoSuchEntityFoundException;
import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantRepository;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public class ImagePlantRepositoryService implements ImagePlantRepository, PaginatedResultDelegate<List<ImagePlant>> {
    
    private ImagePlantAccess imagePlantAccess;
    private ImagePlantFactoryService imagePlantFactory;
    private ImageRepositoryService imageRepository;
    private TemplateRepositoryService templateRepository;

    public ImagePlantRepositoryService(ImagePlantAccess imagePlantAccess,
            ImagePlantFactoryService imagePlantFactory,
            ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository) {
        this.imagePlantAccess = imagePlantAccess;
        this.imagePlantFactory = imagePlantFactory;
        this.imageRepository = imageRepository;
        this.templateRepository = templateRepository;
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
                objectSegment, imageRepository, templateRepository);
    }
    
    private void processTemplates(ImagePlantRoot imagePlant) {
        List<TemplateEntity> templates = imagePlant.getDirtyTemplates();
        for (TemplateEntity template: templates) {
            if (!template.isVoid()) {
                templateRepository.storeTemplate(template);
            } else {
                templateRepository.removeTemplate(template);
            }
            
        }
    }
    
    private void processImages(ImagePlantRoot imagePlant) {
        List<ImageEntity> images = imagePlant.getDirtyImages();
        for (ImageEntity image: images) {
            if (!image.isVoid()) {
                imageRepository.storeImage(image);
            } else {
                imageRepository.removeImage(image);
            }
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
        ImagePlant entity = imagePlantFactory.reconstituteImagePlant(
                objectSegment, imageRepository, templateRepository);
        if (null == entity) {
            throw new NoSuchEntityFoundException("ImagePlant", id);
        }
        return entity;
    }

    @Override
    public PaginatedResult<List<ImagePlant>> findAllImagePlants() {
        PaginatedResult<List<ImagePlantOS>> osResult = imagePlantAccess.selectAllImagePlants();
        return new PaginatedResult<List<ImagePlant>>(
                this, "getAllImagePlants", new Object[] {osResult}) {};
    }
    
    private List<ImagePlant> getAllImagePlants(PaginatedResult<List<ImagePlantOS>> osResult,
            Object pageCursor) {
        List<ImagePlantOS> objectSegments = osResult.getResult(pageCursor);
        List<ImagePlant> imagePlants = new ArrayList<ImagePlant>(objectSegments.size());
        for (ImagePlantOS os: objectSegments) {
            imagePlants.add(imagePlantFactory.reconstituteImagePlant(
                    os, imageRepository, templateRepository));
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
        if ("getAllImagePlants".equals(methodName)) {
            PaginatedResult<List<ImagePlantOS>> osResult = (PaginatedResult<List<ImagePlantOS>>) arguments[0];
            return getAllImagePlants(osResult, pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<ImagePlant> result) {
        if ("getAllImagePlants".equals(tag)) {
            PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[0];
            return osResult.getNextPageCursor();
        }
        throw new UnsupportedOperationException(tag);
    }

}
