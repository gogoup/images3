package com.images3.core.infrastructure;

import java.util.Date;

import com.images3.common.AmazonS3Bucket;
import com.images3.core.infrastructure.ImagePlantOS;
import com.images3.core.infrastructure.spi.ImagePlantAccess;

public class ImagePlantAccessImplMongoDBTest {
    
    private static ObjectSegmentAccessProvider provider;

    public static void main(String[] args) {
        String pathToConfig = ImagePlantAccessImplMongoDBTest.class.getResource("/config.properties").getPath();
        provider = new ObjectSegmentAccessProvider(pathToConfig);

        ImagePlantAccess imagePlantAccess = provider.getImagePlantAccess();
        String id  = imagePlantAccess.genertateImagePlantId();
        System.out.println("ID: " + id);
        if (!imagePlantAccess.isDuplicatedImagePlantName("TestImagePlant")) {
            imagePlantAccess.insertImagePlant(new ImagePlantOS(id, "TestImagePlant", new Date(),
                    new AmazonS3Bucket("AccessKey123", "SecretKey123" , "TestAccessKey"), "MasterTemplateName"));
        }
        //ImagePlantOS imagePlant = imagePlantAccess.selectImagePlantById("ID123");
    }
}
