package user.com.user.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data


public class Configuration {
    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${google.auth-endpoint}")
    private String googleAutEndpoints;

    @Value("${google.scope}")
    private String googleScoope;

}
