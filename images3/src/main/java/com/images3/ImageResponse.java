package com.images3;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ImageResponse {

    private ImageIdentity id;
    private File content;
    private Date dateTime;
    private List<String> templateIds;
    
    public ImageResponse() {}
    
    public ImageResponse(ImageIdentity id, File content, Date dateTime,
            List<String> templateIds) {
        this.id = id;
        this.content = content;
        this.dateTime = dateTime;
        this.templateIds = templateIds;
    }
    public ImageIdentity getId() {
        return id;
    }
    public File getContent() {
        return content;
    }
    public Date getDateTime() {
        return dateTime;
    }
    public List<String> getTemplateIds() {
        return templateIds;
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
                + ((templateIds == null) ? 0 : templateIds.hashCode());
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
        ImageResponse other = (ImageResponse) obj;
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
        if (templateIds == null) {
            if (other.templateIds != null)
                return false;
        } else if (!templateIds.equals(other.templateIds))
            return false;
        return true;
    }
    
    
}
