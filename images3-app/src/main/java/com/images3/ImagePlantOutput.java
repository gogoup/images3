package com.images3;

import java.util.Date;

public class ImagePlantOutput {

    private String id;
    private String name;
    private Date creationTime;
    private UserAccountOutput userAccount;
    
    public ImagePlantOutput(String id, String name, Date creationTime,
            UserAccountOutput userAccount) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.userAccount = userAccount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public UserAccountOutput getUserAccount() {
        return userAccount;
    }
    
}
