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

import com.images3.common.DirtyMark;
import com.images3.common.ImageMetadata;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;
import com.images3.core.Version;
import com.images3.core.infrastructure.ImageOS;

public class ImageEntity extends DirtyMark implements Image {
    
    private ImagePlantRoot imagePlant;
    private ImageOS objectSegment;
    private File imageContent;
    private ImageRepositoryService imageRepository;
    private TemplateRepositoryService templateRepository;
    private Version version;
    
    public ImageEntity(ImagePlantRoot imagePlant, ImageOS objectSegment,
            File imageContent, ImageRepositoryService imageRepository,
            TemplateRepositoryService templateRepository, Version version) {
        this.imagePlant = imagePlant;
        this.objectSegment = objectSegment;
        this.imageContent = imageContent;
        this.imageRepository = imageRepository;
        this.templateRepository = templateRepository;
        this.version = version;
    }
    
    public ImageOS getObjectSegment() {
        return objectSegment;
    }

    @Override
    public ImagePlant getImagePlant() {
        return imagePlant;
    }

    @Override
    public String getId() {
        return getObjectSegment().getId().getImageId();
    }

    @Override
    public File getContent() {
        if (null == imageContent) {
            imageContent = imageRepository.findImageContent(this);
        }
        return imageContent;
    }

    @Override
    public Date getDateTime() {
        return getObjectSegment().getDateTime();
    }

    
    @Override
    public Version getVersion() {
        if (null == version) {
            Image originalImage = getOriginalImage();
            Template template = getTemplate();
            version = new Version(template, originalImage);
        }
        return version;
    }
    
    private Image getOriginalImage() {
        if (getObjectSegment().getVersion().getOriginalImageId() != null) {
            return imageRepository.findImageById(
                    imagePlant, getObjectSegment().getVersion().getOriginalImageId());
        }
        return null;
    }
    
    private Template getTemplate() {
        return templateRepository.findTemplateByName(
                imagePlant, getObjectSegment().getVersion().getTemplateName());
    }

    @Override
    public ImageMetadata getMetadata() {
        return getObjectSegment().getMetadata();
    }

}
