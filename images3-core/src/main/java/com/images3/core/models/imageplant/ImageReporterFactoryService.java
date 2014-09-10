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
