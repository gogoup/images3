package com.images3.core.infrastructure;

import java.util.Date;

import com.images3.AmazonS3Bucket;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;

public class ImagePlantAccessImplMongoDBTest {
    
    private static final ObjectSegmentAccessProvider provider = new ObjectSegmentAccessProvider();

    public static void main(String[] args) {
        ImagePlantAccess imagePlantAccess = provider.getImagePlantAccess();
        String id  = imagePlantAccess.genertateImagePlantId();
        System.out.println("ID: " + id);
        if (!imagePlantAccess.isDuplicatedImagePlantName("TestImagePlant")) {
            imagePlantAccess.insertImagePlant(new ImagePlantOS(id, "TestImagePlant", new Date(),
                    new AmazonS3Bucket("AccessKey123", "SecretKey123" , "TestAccessKey")));
        }
        //ImagePlantOS imagePlant = imagePlantAccess.selectImagePlantById("ID123");
    }
}
