package com.images3;

import com.images3.common.ImageReportType;
import com.images3.common.TimeInterval;

public class ImageReportQueryRequest {
    
    private String imagePlantId;
    private String templateName;
    private TimeInterval interval;
    private ImageReportType type;
    
    public ImageReportQueryRequest(String imagePlantId,
            TimeInterval interval, ImageReportType type) {
        this(imagePlantId, null, interval, type);
    }
    
    public ImageReportQueryRequest(String imagePlantId, String templateName,
            TimeInterval interval, ImageReportType type) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.interval = interval;
        this.type = type;
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

    public ImageReportType getType() {
        return type;
    }
    
    
    
}
