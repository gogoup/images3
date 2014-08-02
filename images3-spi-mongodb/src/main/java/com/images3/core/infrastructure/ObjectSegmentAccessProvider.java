package com.images3.core.infrastructure;

import com.images3.core.infrastructure.spi.ImageAccess;
import com.images3.core.infrastructure.spi.ImagePlantAccess;
import com.images3.core.infrastructure.spi.TemplateAccess;
import com.images3.core.infrastructure.spi.VersionAccess;

public class ObjectSegmentAccessProvider {
    
    private MongoClientAdmin mongoClientAdmin;
    private MongoDBObjectMapper objectMapper;
    
    public ObjectSegmentAccessProvider() {
        mongoClientAdmin = new MongoClientAdmin();
        objectMapper = new MongoDBObjectMapper();
    }

    public ImagePlantAccess getImagePlantAccess() {
        return new ImagePlantAccessImplMongoDB(mongoClientAdmin, objectMapper);
    }
    
    public ImageAccess getImageAccess() {
        return new ImageAccessImplMongoDB(mongoClientAdmin, objectMapper);
    }
    
    public TemplateAccess getTemplateAccess() {
        return new TemplateAccessImplMongoDB(mongoClientAdmin, objectMapper);
    }
    
    public VersionAccess getVersionAccess() {
        return new VersionAccessImplMongoDB(mongoClientAdmin, objectMapper);
    }
}
