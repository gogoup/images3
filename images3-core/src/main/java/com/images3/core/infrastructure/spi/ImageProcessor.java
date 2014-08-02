package com.images3.core.infrastructure.spi;

import java.io.File;

import com.images3.ImageMetadata;
import com.images3.ResizingConfig;
import com.images3.core.infrastructure.ImageOS;

public interface ImageProcessor {

    public boolean isSupportedFormat(File imageFile);
    
    public ImageMetadata readImageMetadata(File imageFile);

    public File resizeImage(String imageId, ImageOS image, File imageFile, ResizingConfig resizingConfig);
}
