package com.images3.core.infrastructure;

import com.images3.ResizingConfig;
import com.images3.TemplateIdentity;

public class TemplateOS {

    private TemplateIdentity id;
    private String name;
    private boolean isArchived;
    private boolean isRemovable;
    private ResizingConfig resizingConfig;
    
    public TemplateOS(TemplateIdentity id, String name, boolean isArchived,
            boolean isRemovable, ResizingConfig resizingConfig) {
        this.id = id;
        this.name = name;
        this.isArchived = isArchived;
        this.isRemovable = isRemovable;
        this.resizingConfig = resizingConfig;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isArchived() {
        return isArchived;
    }
    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }
    public boolean isRemovable() {
        return isRemovable;
    }
    public void setRemovable(boolean isRemovable) {
        this.isRemovable = isRemovable;
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
        TemplateOS other = (TemplateOS) obj;
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
