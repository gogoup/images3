package com.images3.exceptions;

import com.images3.common.TemplateIdentity;

public class UnarchivedTemplateException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4425063883682752547L;
    
    private TemplateIdentity id;
    
    public UnarchivedTemplateException(TemplateIdentity id) {
        super("");
        this.id = id;
    }

    public TemplateIdentity getId() {
        return id;
    }

    
}
