package com.example.demo.services;

import com.example.demo.DTOs.auth.LoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public Authentication login(LoginRequestDTO loginRequestDTO) throws AuthenticationException {
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequestDTO.getEmail(), loginRequestDTO.getPassword()
            )
        );
    }
}
