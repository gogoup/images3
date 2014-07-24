package com.images3.domain;

public interface UserAccountFactory {

    public UserAccount generateUserAccount(String email, String password);
    
}
