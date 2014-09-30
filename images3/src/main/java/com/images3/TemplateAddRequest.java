package com.images3;

import com.images3.common.ResizingConfig;
import com.images3.common.TemplateIdentity;

public class TemplateAddRequest {

    private TemplateIdentity id;
    private ResizingConfig resizingConfig;
    
    public TemplateAddRequest(TemplateIdentity id,
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

}
