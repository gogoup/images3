/*******************************************************************************
 * Copyright 2014 Rui Sun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.images3.data.impl;

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
import com.images3.data.spi.ImageContentAccess;
import com.images3.exceptions.NoSuchEntityFoundException;

public class ImageContentAccessImplS3 implements ImageContentAccess {
    
    private String imageContentDownloadDir;
    private AmazonS3ClientPool clients;
    
    public ImageContentAccessImplS3(String imageContentDownloadDir, AmazonS3ClientPool clients) {
        checkForDirExistence(imageContentDownloadDir);
        this.imageContentDownloadDir = imageContentDownloadDir;
        this.clients = clients;
    }
    
    private void checkForDirExistence(String path) {
        File folder = new File(path);
        if (!folder.exists() 
                || !folder.isDirectory()) {
            throw new IllegalArgumentException("Directory doesn't exists " + path);
        }
    }

    @Override
    public boolean testBucketAccessibility(AmazonS3Bucket bucket) {
        try {
            AmazonS3 client = clients.getClient(bucket);
            client.getBucketLocation(bucket.getName());
        } catch (AmazonClientException e) {
            return false;
        }
        return true;
    }

    @Override
    public File insertImageContent(ImageIdentity id, AmazonS3Bucket bucket,
            File content) {
        File imageFile = new File(generateFilePath(id));
        content.renameTo(imageFile);
        AmazonS3 client = clients.getClient(bucket);
        client.putObject(
                new PutObjectRequest(
                        bucket.getName(), 
                        generateS3ObjectKey(id), 
                        imageFile));
        return imageFile;
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
        if (objList.getObjectSummaries().size() > 0) {
            deleteAllImageContent(client, bucket, objList);
        } else {
            client.deleteObject(new DeleteObjectRequest(bucket.getName(), imagePlantId));
        }
    }
    
    private void deleteAllImageContent(AmazonS3 client, AmazonS3Bucket bucket, ObjectListing objList) {
        boolean isFinished = false;
        while (!isFinished) {
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucket.getName());
            List<KeyVersion> keys = new ArrayList<KeyVersion>(objList.getMaxKeys());
            for (S3ObjectSummary sum: objList.getObjectSummaries()) {
                keys.add(new KeyVersion(sum.getKey()));
            }
            request.setKeys(keys);
            client.deleteObjects(request);
            if (objList.isTruncated()) {
                objList = client.listNextBatchOfObjects(objList);
            } else {
                isFinished = true;
            }
        }
    }
    
    @Override
    public File selectImageContent(ImageIdentity id, AmazonS3Bucket bucket) {
        File imageContent = new File(generateFilePath(id));
        if (imageContent.exists()) {
            return imageContent;
        }
        AmazonS3 client = clients.getClient(bucket);
        try {
            client.getObject(
                    new GetObjectRequest(bucket.getName(), generateS3ObjectKey(id)),
                    imageContent);
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new NoSuchEntityFoundException(
                        "ImageContent", generateS3ObjectKey(id), "No such image content found.");
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
