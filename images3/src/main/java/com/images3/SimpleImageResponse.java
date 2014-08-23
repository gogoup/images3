package com.images3;

import java.util.Date;

import com.images3.common.ImageIdentity;
import com.images3.common.ImageMetadata;
import com.images3.common.ImageVersion;

public class SimpleImageResponse {

    private ImageIdentity id;
    private Date dateTime;
    private ImageVersion version;
    private ImageMetadata metadata;
    
    public SimpleImageResponse() {}

    public SimpleImageResponse(ImageIdentity id, Date dateTime, ImageVersion version,
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((metadata == null) ? 0 : metadata.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleImageResponse other = (SimpleImageResponse) obj;
        if (dateTime == null) {
            if (other.dateTime != null)
                return false;
        } else if (!dateTime.equals(other.dateTime))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    
}
