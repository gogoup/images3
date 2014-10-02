package com.images3.core.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.images3.core.infrastructure.spi.ImageContentAccess;

public class ImageContentAccessProvider {
    
    private Properties config;
    private AmazonS3ClientPool amazonS3ClientPool;
        
    public ImageContentAccessProvider(String pathToConfig) {
        this(readConfigProperties(pathToConfig));
    }
    
    public ImageContentAccessProvider(Properties config) {
        this.config = config;
        amazonS3ClientPool = new AmazonS3ClientPool();
    }
    
    private static Properties readConfigProperties(String pathToConfig) {
        Properties config = new Properties();
        try {
            InputStream in = new FileInputStream(new File(pathToConfig));
            config.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public ImageContentAccess getImageContentAccess() {
        String dir = config.getProperty("imagecontent.download.dir");
        return new ImageContentAccessImplS3(dir, amazonS3ClientPool);
    }
}
