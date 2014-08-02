package com.images3;

public class AmazonS3Bucket {

    private String accessKey;
    private String secretKey;
    private String name;
    
    public AmazonS3Bucket(String accessKey, String secretKey, String name) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.name = name;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((accessKey == null) ? 0 : accessKey.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((secretKey == null) ? 0 : secretKey.hashCode());
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
        AmazonS3Bucket other = (AmazonS3Bucket) obj;
        if (accessKey == null) {
            if (other.accessKey != null)
                return false;
        } else if (!accessKey.equals(other.accessKey))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (secretKey == null) {
            if (other.secretKey != null)
                return false;
        } else if (!secretKey.equals(other.secretKey))
            return false;
        return true;
    }
    
    
    
}
