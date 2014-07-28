package com.images3.core.models;

import java.util.ArrayList;
import java.util.List;

import com.images3.common.PaginatedResult;
import com.images3.common.PaginatedResultDelegate;
import com.images3.core.Image;
import com.images3.core.infrastructure.data.ImageOS;
import com.images3.core.infrastructure.data.spi.ImageAccess;

public class ImageRepositoryService implements PaginatedResultDelegate<List<Image>> {
    
    private ImageAccess imageAccess;
    private ImageFactoryService imageFactory;
    private VersionRepositoryService versionRepository;
    
    public ImageRepositoryService(ImageAccess imageAccess,
            ImageFactoryService imageFactory, 
            VersionRepositoryService versionRepository) {
        this.imageAccess = imageAccess;
        this.imageFactory = imageFactory;
        this.versionRepository = versionRepository;
    }

    public ImageEntity storeImage(ImageEntity image) {
        checkIfVoid(image);
        ImageOS objectSegment = image.getObjectSegment();
        if (image.isNew()) {
            imageAccess.insertImage(objectSegment);
        }
        image.cleanMarks();
        return imageFactory.reconstituteImage(
                (ImagePlantRoot) image.getImagePlant(), objectSegment, versionRepository);
    }
    
    public void removeImage(ImageEntity image) {
        checkIfVoid(image);
        versionRepository.removeVersionsByImage(image); //remove versions
        ImageOS objectSegment = image.getObjectSegment();
        imageAccess.deleteImage(objectSegment);
        image.markAsVoid();
    }
    
    public void removeImages(ImagePlantRoot imagePlant) {
        PaginatedResult<List<Image>> result = findAllImages(imagePlant);
        Object pageCursor = result.getNextPageCursor();
        while (null != pageCursor) {
            List<Image> images = result.getResult(pageCursor);
            for (Image img: images) {
                ImageEntity image = (ImageEntity) img;
                versionRepository.removeVersionsByImage(image);
                imageAccess.deleteImage(image.getObjectSegment());
            }
            pageCursor = result.getNextPageCursor(); //next page.
        }
    }
    
    public Image findImageById(ImagePlantRoot imagePlant, String id) {
        ImageOS objectSegment = imageAccess.selectImageById(imagePlant.getId(), id);
        return imageFactory.reconstituteImage(imagePlant, objectSegment, versionRepository);
    }
    
    public PaginatedResult<List<Image>> findAllImages(ImagePlantRoot imagePlant) {
        PaginatedResult<List<ImageOS>> osResult = 
                imageAccess.selectImagesByImagePlantId(imagePlant.getId());
        return new PaginatedResult<List<Image>>(
                this, "getAllImages", new Object[] {imagePlant, osResult}) {};
    }
    
    private List<Image> getAllImages(ImagePlantRoot imagePlant, 
            PaginatedResult<List<ImageOS>> osResult, Object pageCursor) {
        List<ImageOS> objectSegments = osResult.getResult(pageCursor);
        List<Image> images = new ArrayList<Image>(objectSegments.size());
        for (ImageOS os: objectSegments) {
            images.add(imageFactory.reconstituteImage(imagePlant, os, versionRepository));
        }
        return images;
    }
    
    private void checkIfVoid(ImageEntity image) {
        if (image.isVoid()) {
            throw new IllegalStateException(image.toString());
        }
    }

    @Override
    public Object getNextPageCursor(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getAllImages".equals(methodName)) {
            PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[1];
            return osResult.getNextPageCursor();
        }
        throw new UnsupportedOperationException(methodName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Image> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getAllImages".equals(methodName)) {
            ImagePlantRoot imagePlant = (ImagePlantRoot) arguments[0];
            PaginatedResult<List<ImageOS>> osResult = (PaginatedResult<List<ImageOS>>) arguments[1];
            getAllImages(imagePlant, osResult, pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }
    
}
