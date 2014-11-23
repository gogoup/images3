/*******************************************************************************
 * Copyright 2014 Rui Sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.images3.core.models.imageplant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ImageIdentity;
import com.images3.common.ImageVersion;
import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;
import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImageContentAccess;
import com.images3.core.infrastructure.spi.ImageMetricsService;
import com.images3.exceptions.IllegalImageVersionException;
import com.images3.exceptions.NoSuchEntityFoundException;

import org.gogoup.dddutils.pagination.AutoPaginatedResultDelegate;
import org.gogoup.dddutils.pagination.PaginatedResult;

public class ImageRepositoryService extends AutoPaginatedResultDelegate<List<Image>> {
    
    private ImageAccess imageAccess;
    private ImageContentAccess imageContentAccess;
    private ImageFactoryService imageFactory;
    private TemplateRepositoryService templateRepository;
    private ImageMetricsService imageMetricsService;
    
    public ImageRepositoryService(ImageAccess imageAccess, ImageContentAccess imageContentAccess,
            ImageFactoryService imageFactory, TemplateRepositoryService templateRepository,
            ImageMetricsService imageMetricsService) {
        super(1, "getAllImages");
        this.imageAccess = imageAccess;
        this.imageContentAccess = imageContentAccess;
        this.imageFactory = imageFactory;
        this.templateRepository = templateRepository;
        this.imageMetricsService = imageMetricsService;
    }
    
    public boolean validateBucket(AmazonS3Bucket bucket) {
        return imageContentAccess.testBucketAccessibility(bucket);
    }

    public ImageEntity storeImage(ImageEntity image) {
        checkIfVoid(image);
        ImagePlantRoot imagePlant = (ImagePlantRoot) image.getImagePlant();
        ImageOS objectSegment = image.getObjectSegment();
        File content = image.getContent();
        if (image.isNew()) {
            imageAccess.insertImage(objectSegment); //insert image
            content = imageContentAccess.insertImageContent(
                    objectSegment.getId(), 
                    imagePlant.getObjectSegment().getAmazonS3Bucket(), 
                    image.getContent()); //insert content
            imageMetricsService.recordInbound(objectSegment); //record metrics of image.
        }
        image.cleanMarks();
        return imageFactory.reconstituteImage(
                imagePlant, objectSegment, content, this, templateRepository, image.getVersion());
    }
   
    
    public void removeImage(ImageEntity image) {
        ImagePlantRoot imagePlant = (ImagePlantRoot) image.getImagePlant();
        ImageOS objectSegment = image.getObjectSegment();
        imageContentAccess.deleteImageContent(
                objectSegment.getId(), 
                imagePlant.getObjectSegment().getAmazonS3Bucket()); //delete content
        imageAccess.deleteImage(objectSegment); //delete image
        image.markAsVoid();
    }
    
    public void removeImages(ImagePlantRoot imagePlant) {
        imageAccess.deleteImages(imagePlant.getId());
        imageContentAccess.deleteImageContentByImagePlantId(
                imagePlant.getId(), 
                imagePlant.getObjectSegment().getAmazonS3Bucket());
    }
    
    public Image findImageById(ImagePlantRoot imagePlant, String id) {
        ImageOS objectSegment = imageAccess.selectImageById(new ImageIdentity(imagePlant.getId(), id));
        Image entity = imageFactory.reconstituteImage(
                imagePlant, objectSegment, null, this, templateRepository, null);
        checkForNullImage(entity, id);
        return entity;
    }
    
    private void checkForNullImage(Image image, String id) {
        if (null == image) {
            throw new NoSuchEntityFoundException("Image", id, "No such Image found.");
        }
    }
    
    public boolean hasImageVersion(ImagePlantRoot imagePlant, Version version) {
        Image image = version.getOriginalImage();
        Template template = version.getTemplate();
        checkForNonMasterImage(image);
        if (template.getName().equalsIgnoreCase(Template.MASTER_TEMPLATE_NAME)) {
            return true;
        }
        ImageVersion ver = new ImageVersion(
                template.getName(), image.getId());
        return imageAccess.isDuplicateVersion(imagePlant.getId(), ver);
    }
    
    private void checkForNonMasterImage(Image image) {
        if (!image.getVersion().isMaster()) {
            ImageVersion imageVersion = new ImageVersion(
                    image.getVersion().getTemplate().getName(), image.getId());
            String message = "Master version image only.";
            throw new IllegalImageVersionException(imageVersion, message);
        }
    }
    
    public Image findImageByVersion(ImagePlantRoot imagePlant, Version version) {
        Image image = version.getOriginalImage();
        Template template = version.getTemplate();
        if (image.getVersion().isMaster()
                && template.getName().equalsIgnoreCase(Template.MASTER_TEMPLATE_NAME)) {
            return image;
        }
        ImageVersion ver = new ImageVersion(template.getName(), image.getId());
        ImageOS objectSegment = imageAccess.selectImageByVersion(imagePlant.getId(), ver);
        Image entity = imageFactory.reconstituteImage(
                imagePlant, objectSegment, null, this, templateRepository, version);
        checkForNullImage(entity, version.toString());
        return entity;
    }
    
    public PaginatedResult<List<Image>> findVersioningImages(ImagePlantRoot imagePlant, 
            Image originalImage) {
        if (originalImage.getVersion().getOriginalImage() != null) {
            return new PaginatedResult<List<Image>>(
                    this, "getAllImages_Empty", new Object[] {}); 
        }
        PaginatedResult<List<ImageOS>> osResult = 
                imageAccess.selectImagesByOriginalImageId(imagePlant.getId(), originalImage.getId());
        return new PaginatedResult<List<Image>>(
                this, "getAllImages", new Object[] {imagePlant, osResult});
    }
    
    public PaginatedResult<List<Image>> findAllImages(ImagePlantRoot imagePlant) {
        PaginatedResult<List<ImageOS>> osResult = 
                imageAccess.selectImagesByImagePlantId(imagePlant.getId());
        return new PaginatedResult<List<Image>>(
                this, "getAllImages", new Object[] {imagePlant, osResult});
    }
    
    public PaginatedResult<List<Image>> findImagesByTemplate(ImagePlantRoot imagePlant,
            Template template) {
        PaginatedResult<List<ImageOS>> osResult = 
                imageAccess.selectImagesByTemplateName(imagePlant.getId(), template.getName());
        return new PaginatedResult<List<Image>>(
                this, "getAllImages", new Object[] {imagePlant, osResult});
    }
    
    private List<Image> getAllImages(ImagePlantRoot imagePlant, List<ImageOS> objectSegments) {
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
    public List<Image> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getAllImages_Empty".equals(tag)) {
            return new ArrayList<Image>(0);
        }
        if ("getAllImages".equals(tag)) {
            ImagePlantRoot imagePlant = (ImagePlantRoot) arguments[0];
            PaginatedResult<List<ImageOS>> osResult = (PaginatedResult<List<ImageOS>>) arguments[1];
            List<ImageOS> objectSegments = osResult.getResult(pageCursor);
            return getAllImages(imagePlant, objectSegments);
        }
        throw new UnsupportedOperationException(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Image> fetchAllResults(String tag, Object[] arguments) {
        if ("getAllImages".equals(tag)) {
            ImagePlantRoot imagePlant = (ImagePlantRoot) arguments[0];
            PaginatedResult<List<ImageOS>> osResult = (PaginatedResult<List<ImageOS>>) arguments[1];
            List<ImageOS> objectSegments = osResult.getAllResults();
            return getAllImages(imagePlant, objectSegments);
        }
        throw new UnsupportedOperationException(tag);
    }

    public File findImageContent(ImageEntity image) {
        ImagePlantRoot imagePlant = (ImagePlantRoot) image.getImagePlant();
        ImageOS imageOS = image.getObjectSegment();
        File content = imageContentAccess.selectImageContent(
                imageOS.getId(),
                imagePlant.getObjectSegment().getAmazonS3Bucket());
        imageMetricsService.recordOutbound(imageOS); //record image metrics
        return content;
    }
}
