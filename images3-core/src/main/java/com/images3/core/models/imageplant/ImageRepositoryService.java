package com.images3.core.models.imageplant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.images3.ImageIdentity;
import com.images3.core.Image;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.VersionOS;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageContentAccess;

import org.gogoup.dddutils.pagination.PaginatedResult;
import org.gogoup.dddutils.pagination.PaginatedResultDelegate;

public class ImageRepositoryService implements PaginatedResultDelegate<List<Image>> {
    
    private ImageAccess imageAccess;
    private ImageContentAccess imageContentAccess;
    private ImageFactoryService imageFactory;
    private TemplateRepositoryService templateRepository;
    
    public ImageRepositoryService(ImageAccess imageAccess, ImageContentAccess imageContentAccess,
            ImageFactoryService imageFactory, TemplateRepositoryService templateRepository) {
        this.imageAccess = imageAccess;
        this.imageContentAccess = imageContentAccess;
        this.imageFactory = imageFactory;
        this.templateRepository = templateRepository;
    }

    public ImageEntity storeImage(ImageEntity image) {
        checkIfVoid(image);
        ImagePlantRoot imagePlant = (ImagePlantRoot) image.getImagePlant();
        ImageOS objectSegment = image.getObjectSegment();
        if (image.isNew()) {
            imageAccess.insertImage(objectSegment); //insert image
            imageContentAccess.insertImageContent(
                    objectSegment.getId(), imagePlant.getAmazonS3Bucket(), image.getContent()); //insert content
        }
        image.cleanMarks();
        return imageFactory.reconstituteImage(
                imagePlant, objectSegment, image.getContent(), this, templateRepository, image.getVersion());
    }
   
    
    public void removeImage(ImageEntity image) {
        checkIfVoid(image);
        ImagePlantRoot imagePlant = (ImagePlantRoot) image.getImagePlant();
        ImageOS objectSegment = image.getObjectSegment();
        imageContentAccess.deleteImageContent(
                objectSegment.getId(), imagePlant.getAmazonS3Bucket()); //delete content
        imageAccess.deleteImage(objectSegment); //delete image
        image.markAsVoid();
    }
    
    public void removeImages(ImagePlantRoot imagePlant) {
        PaginatedResult<List<Image>> result = findAllImages(imagePlant);
        Object pageCursor = result.getNextPageCursor();
        while (null != pageCursor) {
            List<Image> images = result.getResult(pageCursor);
            for (Image img: images) {
                ImageEntity image = (ImageEntity) img;
                removeImage(image);
            }
            pageCursor = result.getNextPageCursor(); //next page.
        }
    }
    
    public Image findImageById(ImagePlantRoot imagePlant, String id) {
        ImageOS objectSegment = imageAccess.selectImageById(new ImageIdentity(imagePlant.getId(), id));
        Image entity = imageFactory.reconstituteImage(
                imagePlant, objectSegment, null, this, templateRepository, null);
        return entity;
    }
    
    public Image findImageByVersion(ImagePlantRoot imagePlant, Version version) {
        VersionOS ver = new VersionOS(
                version.getTemplate().getName(), version.getOriginalImage().getId());
        ImageOS objectSegment = imageAccess.selectImageByVersion(ver);
        Image entity = imageFactory.reconstituteImage(
                imagePlant, objectSegment, null, this, templateRepository, null);
        return entity;
    }
    
    public PaginatedResult<List<Image>> findVersioningImages(ImagePlantRoot imagePlant, 
            Image originalImage) {
        PaginatedResult<List<ImageOS>> osResult = 
                imageAccess.selectImagesByOriginalImageId(null, originalImage.getId());
        return new PaginatedResult<List<Image>>(
                this, "getAllImages", new Object[] {imagePlant, osResult}) {};
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
            images.add(
                    imageFactory.reconstituteImage(
                            imagePlant, os, null, this, templateRepository, null));
        }
        return images;
    }
    
    private void checkIfVoid(ImageEntity image) {
        if (image.isVoid()) {
            throw new IllegalStateException(image.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Image> fetchResult(String methodName, Object[] arguments,
            Object pageCursor) {
        if ("getAllImages".equals(methodName)) {
            ImagePlantRoot imagePlant = (ImagePlantRoot) arguments[0];
            PaginatedResult<List<ImageOS>> osResult = (PaginatedResult<List<ImageOS>>) arguments[1];
            return getAllImages(imagePlant, osResult, pageCursor);
        }
        throw new UnsupportedOperationException(methodName);
    }

    @Override
    public Object getNextPageCursor(String tag, Object[] arguments,
            Object pageCursor, List<Image> result) {
        if ("getAllImages".equals(tag)) {
            PaginatedResult<?> osResult = (PaginatedResult<?>) arguments[1];
            return osResult.getNextPageCursor();
        }
        throw new UnsupportedOperationException(tag);
    }

    public File findImageContent(ImageEntity image) {
        ImagePlantRoot imagePlant = (ImagePlantRoot) image.getImagePlant();
        return imageContentAccess.selectImageContent(
                image.getObjectSegment().getId(), imagePlant.getAmazonS3Bucket());
    }
}
