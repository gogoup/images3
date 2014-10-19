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

import com.images3.core.ImagePlant;
import com.images3.core.ImageReporter;
import com.images3.core.Template;
import com.images3.core.infrastructure.spi.ImageMetricsService;

public class ImageReporterFactoryService {
    
    private ImageMetricsService imageMetricsService;
    
    public ImageReporterFactoryService(ImageMetricsService imageMetricsService) {
        this.imageMetricsService = imageMetricsService;
    }

    public ImageReporter generateImageReporter(ImagePlant imagePlant) {
        return new ImageReporterService(imagePlant, null, imageMetricsService);
    }
    
    public ImageReporter generateImageReporter(Template template) {
        return new ImageReporterService(
                template.getImagePlant(), template, imageMetricsService);
    }
}
