package com.images3.infrastructure.data;

import java.util.Date;

public class ImagePlantOS {

    private String id;
    private String name;
    private Date creationTime;
    
    public ImagePlantOS(String id, String name, Date creationTime) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationTime() {
        return creationTime;
    }
    
}
