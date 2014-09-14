package com.images3.core;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.images3.common.ImageReportType;

public class ImageReport {
    
    private ImagePlant imagePlant;
    private Template template;
    private List<Date> times;
    private Map<ImageReportType, List<Long>> values;
    private TimeUnit scale;
    
    public ImageReport(ImagePlant imagePlant, Template template,
            List<Date> times, Map<ImageReportType, List<Long>> values,
            TimeUnit scale) {
        this.imagePlant = imagePlant;
        this.template = template;
        this.times = times;
        this.values = Collections.unmodifiableMap(values);
        this.scale = scale;
    }
    
    public ImagePlant getImagePlant() {
        return imagePlant;
    }
    public Template getTemplate() {
        return template;
    }
    public List<Date> getTimes() {
        return times;
    }
    public Map<ImageReportType, List<Long>> getValues() {
        return values;
    }
    public TimeUnit getScale() {
        return scale;
    }
    
    
}
