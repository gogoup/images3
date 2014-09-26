package com.images3.core.infrastructure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.images3.common.AmazonS3Bucket;
import com.images3.common.ImageIdentity;
import com.images3.core.infrastructure.spi.ImageContentAccess;
import com.images3.exceptions.NoSuchEntityFoundException;

public class ImageContentAccessImplS3 implements ImageContentAccess {
    
    private String imageContentDownloadDir;
    private AmazonS3ClientPool clients;
    
    public ImageContentAccessImplS3(String imageContentDownloadDir, AmazonS3ClientPool clients) {
        this.imageContentDownloadDir = imageContentDownloadDir;
        this.clients = clients;
    }

    @Override
    public boolean testBucketAccessibility(AmazonS3Bucket bucket) {
        try {
            clients.getClient(bucket);
        } catch (AmazonClientException e) {
            return false;
        }
        return true;
    }

    @Override
    public void insertImageContent(ImageIdentity id, AmazonS3Bucket bucket,
            File content) {
        File imageFile = new File(generateFilePath(id));
        content.renameTo(imageFile);
        AmazonS3 client = clients.getClient(bucket);
        client.putObject(
                new PutObjectRequest(
                        bucket.getName(), 
                        generateS3ObjectKey(id), 
                        imageFile));
    }

    @Override
    public void deleteImageContent(ImageIdentity id, AmazonS3Bucket bucket) {
        AmazonS3 client = clients.getClient(bucket);
        client.deleteObject(new DeleteObjectRequest(bucket.getName(), generateS3ObjectKey(id)));
    }

    @Override
    public void deleteImageContentByImagePlantId(String imagePlantId,
            AmazonS3Bucket bucket) {
        AmazonS3 client = clients.getClient(bucket);
        ObjectListing objList = client.listObjects(bucket.getName(), imagePlantId);
        DeleteObjectsRequest request = new DeleteObjectsRequest(bucket.getName());
        List<KeyVersion> keys = new ArrayList<KeyVersion>(objList.getMaxKeys());
        for (S3ObjectSummary sum: objList.getObjectSummaries()) {
            keys.add(new KeyVersion(sum.getKey()));
        }
        request.setKeys(keys);
        client.deleteObjects(request);
    }

    @Override
    public File selectImageContent(ImageIdentity id, AmazonS3Bucket bucket) {
        File imageContent = new File(generateFilePath(id));
        AmazonS3 client = clients.getClient(bucket);
        try {
            client.getObject(
                    new GetObjectRequest(bucket.getName(), generateS3ObjectKey(id)),
                    imageContent);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new NoSuchEntityFoundException("ImageContent", generateS3ObjectKey(id));
            }
            throw new RuntimeException(e);
        }
        return imageContent;
    }
    
    private String generateFilePath(ImageIdentity id) {
       String path = imageContentDownloadDir + File.separator + id.getImagePlantId();
       File folder = new File(path);
       if (!folder.exists()) {
           folder.mkdir();
       }
       return path + File.separator + id.getImageId();
    }
    
    private String generateS3ObjectKey(ImageIdentity id) {
        return id.getImagePlantId() + "/" + id.getImageId();
    }

}
