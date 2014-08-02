package com.images3.core.infrastructure;

import java.io.File;

import com.images3.AmazonS3Bucket;
import com.images3.ImageIdentity;
import com.images3.core.infrastructure.spi.ImageContentAccess;

public class ImageContentAccessImplS3Test {

    private static ImageContentAccess access = new ImageContentAccessProvider().getImageContentAccess();
    
    public static void main(String[] args) {
        delete();
        download();
    }
    
    public static void add() {
        access.insertImageContent(
                new ImageIdentity("123", "456"),
                new AmazonS3Bucket(
                        "AKIAIRNABSSGDJCXC6PQ", 
                        "shy7ZUJC5YjDeWDPXEuAp2rS8knIRCT32RAJgVCw", 
                        "com.images3.images"),
                new File("/Users/ruisun/Desktop/papapanda.jpg"));
    }
    
    public static void delete() {
        access.deleteImageContent(
                new ImageIdentity("123", "456"),
                new AmazonS3Bucket(
                        "AKIAIRNABSSGDJCXC6PQ", 
                        "shy7ZUJC5YjDeWDPXEuAp2rS8knIRCT32RAJgVCw", 
                        "com.images3.images"));
    }
    
    public static void download() {
        File imageContent = access.selectImageContent(
                new ImageIdentity("123", "456"),
                new AmazonS3Bucket(
                        "AKIAIRNABSSGDJCXC6PQ", 
                        "shy7ZUJC5YjDeWDPXEuAp2rS8knIRCT32RAJgVCw", 
                        "com.images3.images"));
        System.out.println("HERE======>" + imageContent.getAbsolutePath());
    }
    
}
