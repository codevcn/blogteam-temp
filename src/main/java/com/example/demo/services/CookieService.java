package com.example.demo.services;

import com.example.demo.configs.props.JWTProps;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Autowired
    private JWTProps jwtProps;

    public String extractCookie(Cookie[] cookies, String cookie_name) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookie_name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void setJWTCookieAtClient(@NonNull HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie(jwtProps.getAUTH_JWT_NAME(), jwt);
        cookie.setMaxAge(jwtProps.getEXPIRATION_TIME_IN_MS() / 1000);
        response.addCookie(cookie);
    }
}
