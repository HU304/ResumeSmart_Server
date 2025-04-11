package com.rs.service;


import com.rs.domain.dto.LoginDto;
import com.rs.response.Result;

public interface LoginService {


    Result login(LoginDto user);
}
