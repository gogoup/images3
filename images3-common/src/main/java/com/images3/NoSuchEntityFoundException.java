package com.images3;

public class NoSuchEntityFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4197063718380029491L;

    private String entityClass;
    private String id;
    
    public NoSuchEntityFoundException(String entityClass, String id) {
        this.entityClass = entityClass;
        this.id = id;
    }

    public String getName() {
        return entityClass;
    }

    public String getId() {
        return id;
    }

}
