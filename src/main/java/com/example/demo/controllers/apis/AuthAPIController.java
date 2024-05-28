package com.example.demo.controllers.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DTOs.UtilDTOs.Success;
import com.example.demo.configs.props.JWTProps;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

@RestController
@RequestMapping("api/auth")
public class AuthAPIController {

    @Autowired
    private JWTProps jwtProps;

    @PostMapping("logout")
    private ResponseEntity<Success> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtProps.getAUTH_JWT_NAME(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(new Success(true));
    }
}
