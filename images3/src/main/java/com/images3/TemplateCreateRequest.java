package com.images3;

public class TemplateCreateRequest {

    private TemplateIdentity id;
    private ResizingConfig resizingConfig;
    
    public TemplateCreateRequest() {}

    public TemplateCreateRequest(TemplateIdentity id,
            ResizingConfig resizingConfig) {
        this.id = id;
        this.resizingConfig = resizingConfig;
    }

    public TemplateIdentity getId() {
        return id;
    }

    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((resizingConfig == null) ? 0 : resizingConfig.hashCode());
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
        TemplateCreateRequest other = (TemplateCreateRequest) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (resizingConfig == null) {
            if (other.resizingConfig != null)
                return false;
        } else if (!resizingConfig.equals(other.resizingConfig))
            return false;
        return true;
    }

}
