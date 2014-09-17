package com.images3.core.infrastructure;

import java.util.Collections;
import java.util.Map;

import com.images3.common.ImageMetricsType;

public class ImageMetricsOS {

    private String imagePlantId;
    private String templateName;
    private long second; //second in timestamp.
    private Map<ImageMetricsType, Long> numbers;
    
    public ImageMetricsOS(String imagePlantId, String templateName,
            long second, Map<ImageMetricsType, Long> numbers) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.second = second;
        this.numbers = Collections.unmodifiableMap(numbers);
    }
    public String getImagePlantId() {
        return imagePlantId;
    }
    public String getTemplateName() {
        return templateName;
    }
    public long getSecond() {
        return second;
    }
    public Map<ImageMetricsType, Long> getNumbers() {
        return numbers;
    }
    
}
