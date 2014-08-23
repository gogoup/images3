package com.images3.core;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ResizingConfig;

public interface ImagePlantFactory {

    public ImagePlant generateImagePlant(String name, 
            AmazonS3Bucket amazonS3Bucket, ResizingConfig resizingConfig);
    
}
