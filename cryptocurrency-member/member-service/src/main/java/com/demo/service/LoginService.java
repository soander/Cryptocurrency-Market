package com.demo.service;

import com.demo.model.LoginForm;
import com.demo.model.LoginUser;

public interface LoginService {

    LoginUser login(LoginForm loginForm);
}
