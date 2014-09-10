package com.images3.core;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ImageReport {
    
    private Date start;
    private Date end;
    private TimeUnit scale;
    private ImageReportType type;
    private List<Long> values;
    
    public ImageReport(Date start, Date end, TimeUnit scale,
            ImageReportType type, List<Long> values) {
        this.start = start;
        this.end = end;
        this.scale = scale;
        this.type = type;
        this.values = values;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public TimeUnit getScale() {
        return scale;
    }

    public ImageReportType getType() {
        return type;
    }

    public List<Long> getValues() {
        return values;
    }
    
}
