package com.images3.core.infrastructure.data;

import java.io.File;
import java.util.Date;

import com.images3.common.ImageMetadata;

public class ImageOS {
    
    private String imagePlantId;
    private String id;
    private File content;
    private Date dateTime;
    private ImageMetadata metadata;
    
    public ImageOS(String imagePlantId, String id, File content, 
            Date dateTime, ImageMetadata metadata) {
        this.imagePlantId = imagePlantId;
        this.id = id;
        this.content = content;
        this.dateTime = dateTime;
        this.metadata = metadata;
    }

    public String getImagePlantId() {
        return imagePlantId;
    }

    public String getId() {
        return id;
    }

    public File getContent() {
        return content;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public ImageMetadata getMetadata() {
        return metadata;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        result = prime * result
                + ((dateTime == null) ? 0 : dateTime.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((imagePlantId == null) ? 0 : imagePlantId.hashCode());
        result = prime * result
                + ((metadata == null) ? 0 : metadata.hashCode());
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
        ImageOS other = (ImageOS) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (!content.equals(other.content))
            return false;
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
        if (imagePlantId == null) {
            if (other.imagePlantId != null)
                return false;
        } else if (!imagePlantId.equals(other.imagePlantId))
            return false;
        if (metadata == null) {
            if (other.metadata != null)
                return false;
        } else if (!metadata.equals(other.metadata))
            return false;
        return true;
    }
    
}
