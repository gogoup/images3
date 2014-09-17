package com.images3;

import com.images3.common.ImageMetricsType;
import com.images3.common.TimeInterval;

public class ImageReportQueryRequest {
    
    private String imagePlantId;
    private String templateName;
    private TimeInterval interval;
    private ImageMetricsType[] types;
    
    public ImageReportQueryRequest(String imagePlantId,
            TimeInterval interval, ImageMetricsType[] types) {
        this(imagePlantId, null, interval, types);
    }

    public ImageReportQueryRequest(String imagePlantId, String templateName,
            TimeInterval interval, ImageMetricsType[] types) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.interval = interval;
        this.types = types;
    }

    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public TimeInterval getInterval() {
        return interval;
    }

    public ImageMetricsType[] getTypes() {
        return types;
    }
    
    
}
