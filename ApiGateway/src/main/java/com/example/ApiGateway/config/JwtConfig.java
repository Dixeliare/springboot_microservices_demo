package com.example.ApiGateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${application.jwt.secret-key}")
    private String secretKey;

    @Value("${application.jwt.expiration-ms}")
    private Long expirationMs;

    public String getSecretKey() {
        return secretKey;
    }

    public Long getExpirationMs() {
        return expirationMs;
    }
}
