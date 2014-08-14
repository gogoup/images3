package com.images3;

public class UnremovableTemplateException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 556214905652939287L;
    
    private TemplateIdentity id;
    
    public UnremovableTemplateException(TemplateIdentity id) {
        super(id.getIdentity());
        this.id = id;
    }

    public TemplateIdentity getId() {
        return id;
    }

    
}
