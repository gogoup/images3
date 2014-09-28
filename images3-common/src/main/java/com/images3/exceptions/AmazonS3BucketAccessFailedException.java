package com.images3.exceptions;

import com.images3.common.AmazonS3Bucket;

public class AmazonS3BucketAccessFailedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -9030582169156234268L;
    
    private AmazonS3Bucket bucket;
    
    public AmazonS3BucketAccessFailedException(AmazonS3Bucket bucket, String message) {
        super(message);
        this.bucket = bucket;
    }

    public AmazonS3Bucket getBucket() {
        return bucket;
    }
    
}

