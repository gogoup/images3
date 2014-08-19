package com.images3;

import java.util.List;

import com.images3.core.Image;
import com.images3.core.ImagePlant;
import com.images3.core.Template;

public class AppObjectMapper {

    public ImagePlantResponse mapToResponse(ImagePlant source) {
        return new ImagePlantResponse(
                source.getId(), 
                source.getName(), 
                source.getAmazonS3Bucket(), 
                source.getCreationTime(),
                mapToResponse(source.getMasterTemplate()));
    }
    
    public TemplateResponse mapToResponse(Template source) {
        return new TemplateResponse(
                new TemplateIdentity(
                        source.getImagePlant().getId(), 
                        source.getName()),
                source.isArchived(), 
                source.isRemovable(),
                source.getResizingConfig());
    }
    
    public ImageResponse mapToResponse(Image source, List<String> templateIds) {
        return new ImageResponse(
                new ImageIdentity(
                        source.getImagePlant().getId(), 
                        source.getId()),
                source.getContent(),
                source.getDateTime(),
                templateIds);
    }
}
