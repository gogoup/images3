package com.images3;

public class VersionResponse {

    private VersionIdentity id;
    private ImageResponse versioningImage;
    
    public VersionResponse() {}
    
    public VersionResponse(VersionIdentity id, ImageResponse versioningImage) {
        this.id = id;
        this.versioningImage = versioningImage;
    }

    public VersionIdentity getId() {
        return id;
    }

    public ImageResponse getVersioningImage() {
        return versioningImage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((versioningImage == null) ? 0 : versioningImage.hashCode());
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
        VersionResponse other = (VersionResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (versioningImage == null) {
            if (other.versioningImage != null)
                return false;
        } else if (!versioningImage.equals(other.versioningImage))
            return false;
        return true;
    }
    
}
