package com.images3;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ResizingConfig;

public class ImagePlantAddRequest {

    private String name;
    private AmazonS3Bucket bucket;
    private ResizingConfig resizingConfig; 
    
    public ImagePlantAddRequest() {}

    public ImagePlantAddRequest(String name, AmazonS3Bucket bucket,
            ResizingConfig resizingConfig) {
        this.name = name;
        this.bucket = bucket;
        this.resizingConfig = resizingConfig;
    }

    public String getName() {
        return name;
    }

    public AmazonS3Bucket getBucket() {
        return bucket;
    }

    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((resizingConfig == null) ? 0 : resizingConfig.hashCode());
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
        ImagePlantAddRequest other = (ImagePlantAddRequest) obj;
        if (bucket == null) {
            if (other.bucket != null)
                return false;
        } else if (!bucket.equals(other.bucket))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (resizingConfig == null) {
            if (other.resizingConfig != null)
                return false;
        } else if (!resizingConfig.equals(other.resizingConfig))
            return false;
        return true;
    }

}
