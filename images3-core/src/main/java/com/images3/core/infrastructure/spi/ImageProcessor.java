package com.images3.core.infrastructure.spi;

import java.io.File;

import com.images3.common.ImageMetadata;
import com.images3.common.ResizingConfig;

public interface ImageProcessor {

    public boolean isSupportedFormat(File imageFile);
    
    public ImageMetadata readImageMetadata(File imageFile);

    public File resizeImage(ImageMetadata metadata, File imageFile, ResizingConfig resizingConfig);
}
