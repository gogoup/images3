package com.images3.core.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.images3.core.infrastructure.spi.ImageProcessor;

public class ImageProcessorProvider {
    
    private Properties config;
    
    public ImageProcessorProvider() {
        loadConfigProperties();
    }
    
    private void loadConfigProperties() {
        config = new Properties();
        InputStream in = getClass().getResourceAsStream("/config.properties");
        try {
            config.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImageProcessor getImageProcessor() {
        String dir = config.getProperty("imagecontent.download.dir");
        return new ImageProcessorImplImgscalr(dir);
    }
}
