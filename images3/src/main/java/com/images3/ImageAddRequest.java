package com.images3;

import java.io.File;

public class ImageAddRequest {

    private String imagePlantId;
    private File content;
    
    public ImageAddRequest() {}
    
    public ImageAddRequest(String imagePlantId, File content) {
        this.imagePlantId = imagePlantId;
        this.content = content;
    }
    
    public String getImagePlantId() {
        return imagePlantId;
    }
    
    public File getContent() {
        return content;
    }
    
}
