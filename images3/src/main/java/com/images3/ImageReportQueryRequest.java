package com.images3;

import com.images3.common.ImageReportType;
import com.images3.common.TimeInterval;

public class ImageReportQueryRequest {
    
    private String imagePlantId;
    private String templateName;
    private TimeInterval interval;
    private ImageReportType[] types;
    
    public ImageReportQueryRequest(String imagePlantId,
            TimeInterval interval, ImageReportType[] types) {
        this(imagePlantId, null, interval, types);
    }

    public ImageReportQueryRequest(String imagePlantId, String templateName,
            TimeInterval interval, ImageReportType[] types) {
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

    public ImageReportType[] getTypes() {
        return types;
    }
    
    
}
