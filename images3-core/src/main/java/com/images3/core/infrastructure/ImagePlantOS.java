package com.images3.core.infrastructure;

import java.util.Date;

import com.images3.AmazonS3Bucket;

public class ImagePlantOS {

    private String id;
    private String name;
    private Date creationTime;
    private AmazonS3Bucket amazonS3Bucket;
    
    public ImagePlantOS(String id, String name, Date creationTime,
            AmazonS3Bucket amazonS3Bucket) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.amazonS3Bucket = amazonS3Bucket;
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

    public AmazonS3Bucket getAmazonS3Bucket() {
        return amazonS3Bucket;
    }
    
    public void setAmazonS3Bucket(AmazonS3Bucket amazonS3Bucket) {
        this.amazonS3Bucket = amazonS3Bucket;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((amazonS3Bucket == null) ? 0 : amazonS3Bucket.hashCode());
        result = prime * result
                + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (amazonS3Bucket == null) {
            if (other.amazonS3Bucket != null)
                return false;
        } else if (!amazonS3Bucket.equals(other.amazonS3Bucket))
            return false;
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
        return true;
    }
    
}
