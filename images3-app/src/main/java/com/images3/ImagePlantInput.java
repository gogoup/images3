package com.images3;

public class ImagePlantInput {

    private String name;
    private String userAccountId;
    
    public ImagePlantInput(String name, String userAccountId) {
        this.name = name;
        this.userAccountId = userAccountId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getUserAccountId() {
        return userAccountId;
    }
    
}
