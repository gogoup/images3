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
package com.images3;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetricsType;
import com.images3.common.ImageVersion;
import com.images3.common.SecuredAmazonS3Bucket;
import com.images3.common.TemplateIdentity;
import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;

public class AppObjectMapper {

    public ImagePlantResponse mapToResponse(ImagePlant source) {
        return new ImagePlantResponse(
                source.getId(), 
                source.getName(), 
                new SecuredAmazonS3Bucket(source.getAmazonS3Bucket()), 
                source.getCreationTime(),
                mapToResponse(source.getMasterTemplate()),
                source.countTemplates(),
                source.generateImageReporter().calculate(ImageMetricsType.COUNTS_INBOUND),
                source.getMaximumImageSize());
    }
    
    public TemplateResponse mapToResponse(Template source) {
        return new TemplateResponse(
                new TemplateIdentity(
                        source.getImagePlant().getId(), 
                        source.getName()),
                source.isArchived(), 
                source.isRemovable(),
                source.getResizingConfig());
    }
    
    public ImageResponse mapToResponse(Image source) {
        Image originalImage = source.getVersion().getOriginalImage();
        return new ImageResponse(
                new ImageIdentity(
                        source.getImagePlant().getId(), 
                        source.getId()),
                source.getDateTime(),
                new ImageVersion(
                        source.getVersion().getTemplate().getName(),
                        originalImage == null ? null: source.getVersion().getOriginalImage().getId()),
                source.getMetadata());
    }
    
}
