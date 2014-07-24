package com.images3.domain;

public interface UserAccount {

    public String getId();
    
    public String getUsername();
    
    public void updateUsername(String username);
    
    public String getEmail();
    
    public void updateEmail(String email);
    
    public void changePassword(String oldPassword, String newPassword);
    
    public boolean verifyPassword(String password);
    
}
