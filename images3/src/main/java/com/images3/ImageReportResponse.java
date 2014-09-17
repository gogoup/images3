package com.images3;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.images3.common.ImageMetricsType;

public class ImageReportResponse {
    
    private String imagePlantId;
    private String templateName;
    private List<Date> times;
    private Map<ImageMetricsType, List<Long>> values;
    private TimeUnit scale;
    
    public ImageReportResponse(String imagePlantId, String templateName,
            List<Date> times, Map<ImageMetricsType, List<Long>> values,
            TimeUnit scale) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.times = times;
        this.values = values;
        this.scale = scale;
    }

    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public List<Date> getTimes() {
        return times;
    }

    public Map<ImageMetricsType, List<Long>> getValues() {
        return values;
    }

    public TimeUnit getScale() {
        return scale;
    }
    
   
}
