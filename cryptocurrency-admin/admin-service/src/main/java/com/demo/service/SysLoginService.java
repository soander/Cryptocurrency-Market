package com.demo.service;

//import com.demo.model.LoginResult;

import com.demo.model.LoginResult;

// Login interface
public interface SysLoginService {

    /**
     * login
     * @param username
     * @param password
     * @return login result
     */
    LoginResult login(String username, String password);
}
