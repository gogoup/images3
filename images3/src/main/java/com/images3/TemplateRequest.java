package com.images3;

public class TemplateRequest {

    private String imagePlantId;
    private String name;
    private boolean isArchived;
    private ResizingConfig resizingConfig;
    
    public TemplateRequest() {}
    
    public TemplateRequest(String imagePlantId, String name,
            ResizingConfig resizingConfig) {
        this(imagePlantId, name, false, resizingConfig);
    }
    
    public TemplateRequest(String imagePlantId, String name,
            boolean isArchived, ResizingConfig resizingConfig) {
        this.imagePlantId = imagePlantId;
        this.name = name;
        this.isArchived = isArchived;
        this.resizingConfig = resizingConfig;
    }
    
    public String getImagePlantId() {
        return imagePlantId;
    }
    public String getName() {
        return name;
    }
    public boolean isArchived() {
        return isArchived;
    }
    public ResizingConfig getResizingConfig() {
        return resizingConfig;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((imagePlantId == null) ? 0 : imagePlantId.hashCode());
        result = prime * result + (isArchived ? 1231 : 1237);
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
        TemplateRequest other = (TemplateRequest) obj;
        if (imagePlantId == null) {
            if (other.imagePlantId != null)
                return false;
        } else if (!imagePlantId.equals(other.imagePlantId))
            return false;
        if (isArchived != other.isArchived)
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
