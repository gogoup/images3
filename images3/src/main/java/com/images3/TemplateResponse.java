package com.images3;

public class TemplateResponse {

    private TemplateIdentity id;
    private String name;
    private boolean isArchived;
    private boolean isRemovable;
    private ResizingConfig resizingConfig;
    
    public TemplateResponse(TemplateIdentity id, String name,
            boolean isArchived, boolean isRemovable,
            ResizingConfig resizingConfig) {
        this.id = id;
        this.name = name;
        this.isArchived = isArchived;
        this.isRemovable = isRemovable;
        this.resizingConfig = resizingConfig;
    }

    public TemplateIdentity getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public boolean isRemovable() {
        return isRemovable;
    }

    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + (isArchived ? 1231 : 1237);
        result = prime * result + (isRemovable ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        TemplateResponse other = (TemplateResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (isArchived != other.isArchived)
            return false;
        if (isRemovable != other.isRemovable)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (resizingConfig == null) {
            if (other.resizingConfig != null)
                return false;
        } else if (!resizingConfig.equals(other.resizingConfig))
            return false;
        return true;
    }
    
    
}
