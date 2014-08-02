package com.images3.core;

import com.images3.AmazonS3Bucket;

public interface ImagePlantFactory {

    public ImagePlant generateImagePlant(String name, 
            AmazonS3Bucket amazonS3Bucket);
    
}
