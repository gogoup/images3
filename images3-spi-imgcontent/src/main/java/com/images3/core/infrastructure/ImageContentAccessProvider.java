package com.images3.core.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.images3.core.infrastructure.spi.ImageContentAccess;

public class ImageContentAccessProvider {
    
    private Properties config;
    private AmazonS3ClientPool amazonS3ClientPool;
    
    public ImageContentAccessProvider() {
        loadConfigProperties();
        amazonS3ClientPool = new AmazonS3ClientPool();
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

    public ImageContentAccess getImageContentAccess() {
        String dir = config.getProperty("imagecontent.download.dir");
        return new ImageContentAccessImplS3(dir, amazonS3ClientPool);
    }
}
