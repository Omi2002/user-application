package user.com.user.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "app.auth")
public class AppConfig {

    @Value("${authtoken}")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }
}
