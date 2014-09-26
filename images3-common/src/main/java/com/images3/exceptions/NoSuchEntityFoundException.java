package com.images3.exceptions;

public class NoSuchEntityFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4197063718380029491L;

    private String entity;
    private String id;
    
    public NoSuchEntityFoundException(String entity, String id) {
        super(entity + " {" + id + "}");
        this.entity = entity;
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public String getId() {
        return id;
    }

}
