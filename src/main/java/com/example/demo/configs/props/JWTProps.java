package com.example.demo.configs.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "blgtm.jwt")
@Getter
@Setter
public class JWTProps {

    private String AUTH_JWT_NAME;
    private int EXPIRATION_TIME_IN_MS;
    private String SECRET_KEY;
}
