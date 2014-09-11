package com.images3;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.images3.common.ImageReportType;

public class ImageReportResponse {
    
    private String imagePlantId;
    private String templateName;
    private List<Date> times;
    private List<Long> values;
    private TimeUnit scale;
    private ImageReportType type;
    
    public ImageReportResponse(String imagePlantId, String templateName,
            List<Date> times, List<Long> values, TimeUnit scale,
            ImageReportType type) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.times = times;
        this.values = values;
        this.scale = scale;
        this.type = type;
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

    public List<Long> getValues() {
        return values;
    }

    public TimeUnit getScale() {
        return scale;
    }

    public ImageReportType getType() {
        return type;
    }
   
}
