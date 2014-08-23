package com.images3.core.infrastructure.spi;

import java.io.File;

import com.images3.common.AmazonS3Bucket;
import com.images3.common.ImageIdentity;

public interface ImageContentAccess {

    public void insertImageContent(ImageIdentity id, AmazonS3Bucket bucket, File content);
    
    public void deleteImageContent(ImageIdentity id, AmazonS3Bucket bucket);
    
    public void deleteImageContentByImagePlantId(String imagePlantId, AmazonS3Bucket bucket);
    
    public File selectImageContent(ImageIdentity id, AmazonS3Bucket bucket);
    
}
