package com.images3.core;

import java.util.List;

import com.images3.common.PaginatedResult;

public interface UserAccountRepository {

    public UserAccount storeUserAcount(UserAccount userAccount);
    
    public void updateUserAccount(UserAccount userAccount);
    
    public UserAccount findUserAccountById(String id);
    
    public UserAccount findUserAccountByEmail(String email);
    
    public UserAccount findUserAccountByUsername(String username);
    
    public PaginatedResult<List<UserAccount>> findAllUsers();
    
}
