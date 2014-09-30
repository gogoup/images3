package com.images3;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ResizingConfig;

public class ImagePlantAddRequest {

    private String name;
    private AmazonS3Bucket bucket;
    private ResizingConfig resizingConfig; 

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

}
