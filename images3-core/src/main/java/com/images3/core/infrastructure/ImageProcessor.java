package com.images3.core.infrastructure;

import java.io.File;

import com.images3.common.ImageMetadata;
import com.images3.common.ResizingConfig;

public interface ImageProcessor {
    
    public File convertToImageFile(String base64Image);
    
    public ImageMetadata readImageMetadata(File imageFile);

    public File resizeImage(File imageFile, ResizingConfig resizingConfig);
}
