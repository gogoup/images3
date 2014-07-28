package com.images3.core.infrastructure.data;

import java.util.Date;

public class ImagePlantOS {

    private String id;
    private String name;
    private Date creationTime;
    private String userAccountId;
    
    public ImagePlantOS(String id, String name, Date creationTime,
            String userAccountId) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.userAccountId = userAccountId;
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

    public String getUserAccountId() {
        return userAccountId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((userAccountId == null) ? 0 : userAccountId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImagePlantOS other = (ImagePlantOS) obj;
        if (creationTime == null) {
            if (other.creationTime != null)
                return false;
        } else if (!creationTime.equals(other.creationTime))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (userAccountId == null) {
            if (other.userAccountId != null)
                return false;
        } else if (!userAccountId.equals(other.userAccountId))
            return false;
        return true;
    }
    
}
