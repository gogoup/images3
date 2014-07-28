package com.images3;

public class UserAccountOutput {

    private String id;
    private String username;
    private String email;
    
    public UserAccountOutput(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    
}
