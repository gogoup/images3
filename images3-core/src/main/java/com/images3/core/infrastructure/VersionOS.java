package com.images3.core.infrastructure;

import com.images3.VersionIdentity;

public class VersionOS {
    
    private VersionIdentity id;
    private String vesioningImageId;
    
    public VersionOS(VersionIdentity id, String vesioningImageId) {
        this.id = id;
        this.vesioningImageId = vesioningImageId;
    }
    public VersionIdentity getId() {
        return id;
    }
    public String getVesioningImageId() {
        return vesioningImageId;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime
                * result
                + ((vesioningImageId == null) ? 0 : vesioningImageId.hashCode());
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
        VersionOS other = (VersionOS) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (vesioningImageId == null) {
            if (other.vesioningImageId != null)
                return false;
        } else if (!vesioningImageId.equals(other.vesioningImageId))
            return false;
        return true;
    }
    
}
