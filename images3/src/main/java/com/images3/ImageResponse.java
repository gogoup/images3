package com.images3;

import java.util.Date;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetadata;
import com.images3.common.ImageVersion;

public class ImageResponse {

    private ImageIdentity id;
    private Date dateTime;
    private ImageVersion version;
    private ImageMetadata metadata;
    
    public ImageResponse(ImageIdentity id, Date dateTime, ImageVersion version,
            ImageMetadata metadata) {
        this.id = id;
        this.dateTime = dateTime;
        this.version = version;
        this.metadata = metadata;
    }

    public ImageIdentity getId() {
        return id;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public ImageVersion getVersion() {
        return version;
    }

    public ImageMetadata getMetadata() {
        return metadata;
    }
    
}
