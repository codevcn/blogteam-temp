package com.example.demo.configs.security;

import com.example.demo.configs.props.JWTProps;
import com.example.demo.services.CookieService;
import com.example.demo.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private JWTProps jwtProps;

    @Autowired
    private CookieService cookieService;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.printf("\n>>> got a request\n");

        String jwt = extractJWTFromRequest(request);
        String userId = extractUserIdFromJWT(jwt);

        validateJWT(request, userId, jwt);

        filterChain.doFilter(request, response);
    }

    private String extractJWTFromRequest(@NonNull HttpServletRequest request) {
        return cookieService.extractCookie(request.getCookies(), jwtProps.getAUTH_JWT_NAME());
    }

    private String extractUserIdFromJWT(String jwt) {
        if (jwt != null && jwt.isEmpty() == false) {
            try {
                return jwtService.extractUserId(jwt);
            } catch (Exception exception) {
                System.out.printf("\n>>> Fail to verify token: %s\n", exception.getMessage());
            }
        }
        return null;
    }

    private void validateJWT(@NonNull HttpServletRequest request, String userId, String jwt) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userId != null && authentication == null) {
            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            if (jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }
}
