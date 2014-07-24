package com.images3.infrastructure.data;

import java.io.File;

public class ImageOS {
    
    private String imagePlantId;
    private String id;
    private File content;
    
    public ImageOS(String imagePlantId, String id, File content) {
        this.imagePlantId = imagePlantId;
        this.id = id;
        this.content = content;
    }

    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getId() {
        return id;
    }

    public File getContent() {
        return content;
    }

    
}
