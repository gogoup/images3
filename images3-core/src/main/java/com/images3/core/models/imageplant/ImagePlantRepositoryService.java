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

import java.util.ArrayList;
import java.util.List;

import com.images3.core.ImagePlant;
import com.images3.core.ImagePlantRepository;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.exceptions.NoSuchEntityFoundException;

import org.gogoup.dddutils.pagination.AutoPaginatedResultDelegate;
import org.gogoup.dddutils.pagination.PaginatedResult;

public class ImagePlantRepositoryService extends AutoPaginatedResultDelegate<List<ImagePlant>> implements ImagePlantRepository  {
    
    private ImagePlantAccess imagePlantAccess;
    private ImagePlantFactoryService imagePlantFactory;
    private ImageRepositoryService imageRepository;
    private TemplateRepositoryService templateRepository;

    public ImagePlantRepositoryService(ImagePlantAccess imagePlantAccess,
            ImagePlantFactoryService imagePlantFactory,
            ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository) {
        super(0, "getAllImagePlants");
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
        ImagePlantOS objectSegment = root.getObjectSegment();
        imagePlantAccess.deleteImagePlant(objectSegment);
        root.markAsVoid();
        templateRepository.removeTemplates(root);
        imageRepository.removeImages(root);
    }

    @Override
    public ImagePlant findImagePlantById(String id) {
        ImagePlantOS objectSegment = imagePlantAccess.selectImagePlantById(id);
        ImagePlant entity = imagePlantFactory.reconstituteImagePlant(
                objectSegment, imageRepository, templateRepository);
        if (null == entity) {
            throw new NoSuchEntityFoundException("ImagePlant", id, "No such ImagePlant found.");
        }
        return entity;
    }

    @Override
    public PaginatedResult<List<ImagePlant>> findAllImagePlants() {
        PaginatedResult<List<ImagePlantOS>> osResult = imagePlantAccess.selectAllImagePlants();
        return new PaginatedResult<List<ImagePlant>>(
                this, "getAllImagePlants", new Object[] {osResult});
    }
    
    private List<ImagePlant> getAllImagePlants(List<ImagePlantOS> objectSegments) {
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
    public List<ImagePlant> fetchResult(String tag, Object[] arguments,
            Object pageCursor) {
        if ("getAllImagePlants".equals(tag)) {
            PaginatedResult<List<ImagePlantOS>> osResult = (PaginatedResult<List<ImagePlantOS>>) arguments[0];
            List<ImagePlantOS> objectSegments = osResult.getResult(pageCursor);
            return getAllImagePlants(objectSegments);
        }
        throw new UnsupportedOperationException(tag);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ImagePlant> fetchAllResults(String tag, Object[] arguments) {
        if ("getAllImagePlants".equals(tag)) {
            PaginatedResult<List<ImagePlantOS>> osResult = (PaginatedResult<List<ImagePlantOS>>) arguments[0];
            List<ImagePlantOS> objectSegments = osResult.getAllResults();
            return getAllImagePlants(objectSegments);
        }
        throw new UnsupportedOperationException(tag);
    }

}
