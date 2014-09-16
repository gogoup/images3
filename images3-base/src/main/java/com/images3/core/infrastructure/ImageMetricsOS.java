package com.images3.core.infrastructure;

public class ImageMetricsOS {

    private String imagePlantId;
    private String templateName;
    private long second; //second in timestamp.
    private long numberOfImages;
    private long sizeOfImages;
    
    public ImageMetricsOS(String imagePlantId, String templateName, long second,
            long numberOfImages, long sizeOfImages) {
        this.imagePlantId = imagePlantId;
        this.templateName = templateName;
        this.second = second;
        this.numberOfImages = numberOfImages;
        this.sizeOfImages = sizeOfImages;
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

    public long getNumberOfImages() {
        return numberOfImages;
    }

    public long getSizeOfImages() {
        return sizeOfImages;
    }
    
}
