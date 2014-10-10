package com.images3.exceptions;

import com.images3.common.TemplateIdentity;

public class UnachievableTemplateException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4425063883682752547L;
    
    private TemplateIdentity id;
    
    public UnachievableTemplateException(TemplateIdentity id, String message) {
        super(message);
        this.id = id;
    }

    public TemplateIdentity getId() {
        return id;
    }

    
}
