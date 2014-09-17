package com.images3.core;

import com.images3.common.ImageMetricsType;
import com.images3.common.TimeInterval;

public interface ImageReporter {
    
    public long calculate(ImageMetricsType type);
    
    public long calculate(ImageMetricsType type, TimeInterval interval);
    
    public ImageReport fetchReport(ImageMetricsType[] types, TimeInterval interval);
    
}
