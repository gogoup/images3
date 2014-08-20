package com.images3;

public class TemplateUpdateRequest {

    private TemplateIdentity id;
    private boolean isArchived;
    
    public TemplateUpdateRequest() {}

    public TemplateUpdateRequest(TemplateIdentity id, boolean isArchived) {
        this.id = id;
        this.isArchived = isArchived;
    }

    public TemplateIdentity getId() {
        return id;
    }

    public boolean isArchived() {
        return isArchived;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (isArchived ? 1231 : 1237);
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
        TemplateUpdateRequest other = (TemplateUpdateRequest) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (isArchived != other.isArchived)
            return false;
        return true;
    }
    
}
