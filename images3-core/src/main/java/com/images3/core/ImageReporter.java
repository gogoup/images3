package com.images3.core;

import com.images3.common.TimeInterval;

public interface ImageReporter {

    public long countImages();
    
    public long countImages(TimeInterval interval);
    
    public long calculateSizeOfImages();
    
    public long calculateSizeOfImages(TimeInterval interval);
    
    public ImageReport fetchReport(TimeInterval interval);
    
}
