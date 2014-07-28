package com.images3.common;

public class NoSuchEntityFound extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4197063718380029491L;

    private String name;
    private String id;
    
    public NoSuchEntityFound(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    
}
