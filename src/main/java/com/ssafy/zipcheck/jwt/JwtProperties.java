package com.ssafy.zipcheck.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * Base64-encoded secret key used to sign/verify JWT.
     */
    private String secret;
    /**
     * Access token validity in milliseconds.
     */
    private long accessTokenValidity;
    /**
     * HTTP header name that carries the token.
     */
    private String header;
    /**
     * Prefix for the token value (e.g., "Bearer ").
     */
    private String prefix;
    /**
     * Issuer claim value.
     */
    private String issuer;
}
