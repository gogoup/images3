package com.images3;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetricsType;
import com.images3.common.ImageVersion;
import com.images3.common.TemplateIdentity;
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
                mapToResponse(source.getMasterTemplate()),
                source.countTemplates(),
                source.generateImageReporter().calculate(ImageMetricsType.COUNTS_INBOUND));
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
    
    public ImageResponse mapToResponse(Image source) {
        Image originalImage = source.getVersion().getOriginalImage();
        return new ImageResponse(
                new ImageIdentity(
                        source.getImagePlant().getId(), 
                        source.getId()),
                source.getDateTime(),
                new ImageVersion(
                        source.getVersion().getTemplate().getName(),
                        originalImage == null ? null: source.getVersion().getOriginalImage().getId()),
                source.getMetadata());
    }
    
}
