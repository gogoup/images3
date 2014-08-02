package com.images3.core.infrastructure;

import java.io.File;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.images3.AmazonS3Bucket;
import com.images3.ImageIdentity;
import com.images3.NoSuchEntityFoundException;
import com.images3.core.infrastructure.spi.ImageContentAccess;

public class ImageContentAccessImplS3 implements ImageContentAccess {
    
    private String imageContentDownloadDir;
    private AmazonS3ClientPool clients;
    
    public ImageContentAccessImplS3(String imageContentDownloadDir, AmazonS3ClientPool clients) {
        this.imageContentDownloadDir = imageContentDownloadDir;
        this.clients = clients;
    }

    @Override
    public void insertImageContent(ImageIdentity id, AmazonS3Bucket bucket,
            File content) {
        AmazonS3 client = clients.getClient(bucket);
        client.putObject(new PutObjectRequest(bucket.getName(), id.getIdentity(), content));
    }

    @Override
    public void deleteImageContent(ImageIdentity id, AmazonS3Bucket bucket) {
        AmazonS3 client = clients.getClient(bucket);
        client.deleteObject(new DeleteObjectRequest(bucket.getName(), id.getIdentity()));
    }

    @Override
    public File selectImageContent(ImageIdentity id, AmazonS3Bucket bucket) {
        File imageContent = new File(generateFilePath(id));
        AmazonS3 client = clients.getClient(bucket);
        try {
            client.getObject(
                    new GetObjectRequest(bucket.getName(), id.getIdentity()),
                    imageContent);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new NoSuchEntityFoundException("ImageContent", id.getIdentity());
            }
            throw new RuntimeException(e);
        }
        return imageContent;
    }
    
    private String generateFilePath(ImageIdentity id) {
       return imageContentDownloadDir + File.separator + id.getIdentity();
    }

}
