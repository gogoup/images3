package com.images3.core.infrastructure;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.images3.AmazonS3Bucket;

public class AmazonS3ClientPool {

    private static Map<String, AmazonS3> pool = new HashMap<String, AmazonS3>(); //<accessKey_secretKey, AmazonS3>
    
    public AmazonS3ClientPool() {}
    
    private String generateKey(String accessKey, String secretKey) {
        return (accessKey+"_"+secretKey);
    }
    
    public AmazonS3 getClient(AmazonS3Bucket bucket) {
        String key = generateKey(bucket.getAccessKey(), bucket.getSecretKey());
        AmazonS3 client = pool.get(key);
        if (null == client) {
            client =createAmazonS3Client(bucket.getAccessKey(), bucket.getSecretKey());
            pool.put(key, client);
        }
        return client;
    }
    
    private AmazonS3 createAmazonS3Client(String accessKey, String secretKey) {
        return new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
    }
    
}
