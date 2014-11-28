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
import java.util.Date;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetadata;
import com.images3.common.ImageVersion;
import com.images3.core.Image;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.data.ImageOS;
import com.images3.data.ImagePlantOS;
import com.images3.data.spi.ImageAccess;
import com.images3.data.spi.ImageProcessor;
import com.images3.exceptions.DuplicateImageVersionException;
import com.images3.exceptions.IllegalImageVersionException;
import com.images3.exceptions.OverMaximumlmageSizeException;
import com.images3.exceptions.UnsupportedImageFormatException;

public class ImageFactoryService {
    
    private ImageAccess imageAccess;
    private ImageProcessor imageProcessor;
    
    public ImageFactoryService(ImageAccess imageAccess, ImageProcessor imageProcessor) {
        this.imageAccess = imageAccess;
        this.imageProcessor = imageProcessor;
    }

    public ImageEntity generateImage(ImagePlantRoot imagePlant, File imageContent, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        checkForImageSize(imagePlant, imageContent);
        checkForUnsupportedFormat(imageContent);
        
        TemplateEntity template = (TemplateEntity) imagePlant.getMasterTemplate();
        Version version = new Version(template, null);
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        File resizedContent = imageProcessor.resizeImage(
                metadata, 
                imageContent, 
                version.getTemplate().getResizingConfig());
        return generateImage(
                imagePlant, 
                resizedContent,
                version,
                imageRepository, 
                templateRepository);
    }
    
    private void checkForImageSize(ImagePlantRoot imagePlant, File imageContent) {
        if (!imagePlant.isUnlimitImageSize()
                && imageContent.length() > imagePlant.getMaximumImageSize()) {
            throw new OverMaximumlmageSizeException(
                    imagePlant.getMaximumImageSize(), (int) imageContent.length(), "The uploaded image is too big");
        }
    }
    
    private void checkForUnsupportedFormat(File imageContent) {
        if (!imageProcessor.isSupportedFormat(imageContent)) {
            throw new UnsupportedImageFormatException("");
        }
    }
    
    public ImageEntity generateImage(ImagePlantRoot imagePlant, Version version, 
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        checkForNullOriginalImage(version);
        checkForNonMasterOriginalImage(version);
        checkForArchivedTemplate(version.getTemplate());
        checkForMasterVersion(version);
        checkForDuplicateVersion(imagePlant, version);
        
        ImageEntity originalImage = (ImageEntity) version.getOriginalImage();
        File resizedContent = imageProcessor.resizeImage(
                originalImage.getObjectSegment().getMetadata(), 
                originalImage.getContent(), 
                version.getTemplate().getResizingConfig());
        return generateImage(
                imagePlant,  
                resizedContent, 
                version,
                imageRepository,
                templateRepository);
    }
    
    private void checkForArchivedTemplate(Template template) {
        if (template.isArchived()) {
            throw new IllegalArgumentException("Template " + template.getName() + " has been archived.");
        }
    }
    
    private void checkForNullOriginalImage(Version version) {
        if (null == version.getOriginalImage()) {
            throw new NullPointerException("Original image.");
        }
    }
    
    private void checkForNonMasterOriginalImage(Version version) {
        if (!version.getOriginalImage().getVersion().isMaster()) {
            ImageVersion imageVersion = new ImageVersion(
                    version.getTemplate().getName(), version.getOriginalImage().getId());
            String message = "Only master image can be used to generate other versions";
            throw new IllegalImageVersionException(imageVersion, message);
        }
    }
    
    private void checkForMasterVersion(Version version) {
        if (version.getTemplate().getName().equalsIgnoreCase(Template.MASTER_TEMPLATE_NAME)) {
            ImageVersion imageVersion = new ImageVersion(
                    version.getTemplate().getName(), version.getOriginalImage().getId());
            String message = "Image version \'" + TemplateEntity.MASTER_TEMPLATE_NAME + "\' already generated.";
            throw new DuplicateImageVersionException(imageVersion, message);
        }
    }
    
    private void checkForDuplicateVersion(ImagePlantRoot imagePlant, Version version) {
        ImageVersion imageVersion = new ImageVersion(
                version.getTemplate().getName(), version.getOriginalImage().getId());
        if (imageAccess.isDuplicateVersion(imagePlant.getId(), imageVersion)) {
            String message = "Image version \'" + imageVersion.getTemplateName() + "\' already generated.";
            throw new DuplicateImageVersionException(imageVersion, message);
        }
    }
    
    private ImageEntity generateImage(ImagePlantRoot imagePlant, File imageContent, Version version,
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository) {
        ImageVersion versionOS = getVersionOS(version);
        String imageId = imageAccess.generateImageId(imagePlant.getObjectSegment());
        ImageMetadata metadata = imageProcessor.readImageMetadata(imageContent);
        ImageOS objectSegment = generateImageOS(
                imageId, metadata, imagePlant.getObjectSegment(), versionOS);
        ImageEntity entity = reconstituteImage(
                imagePlant, objectSegment, imageContent, imageRepository, templateRepository, version);
        entity.markAsNew();
        return entity;
    }
    
    private ImageVersion getVersionOS(Version version) {
        Template template = version.getTemplate();
        Image originalImage = version.getOriginalImage();
        ImageVersion imageVer = null;
        if (null != originalImage) {
            imageVer = new ImageVersion(template.getName(), originalImage.getId());
        } else {
            imageVer = new ImageVersion(template.getName(), null);
        }
        return imageVer;
    }
    
    private ImageOS generateImageOS(String id, ImageMetadata metadata, 
            ImagePlantOS imagePlantOS, ImageVersion versionOS) {
        Date dateTime = new Date(System.currentTimeMillis());
        ImageOS objectSegment = new ImageOS(
                new ImageIdentity(imagePlantOS.getId(), id), dateTime, metadata, versionOS);
        return objectSegment;
    }
    
    public ImageEntity reconstituteImage(ImagePlantRoot imagePlant, ImageOS objectSegment, File imageContent,
            ImageRepositoryService imageRepository, TemplateRepositoryService templateRepository, Version version) {
        if (null == objectSegment) {
            return null;
        }
        return new ImageEntity(
                imagePlant, objectSegment, imageContent, imageRepository, templateRepository, version);
    }
    
}
