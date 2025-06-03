package user.com.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "payment")
@Data
public class SeatBookingConfig {
    /**
     * Timeout limit for payment simulation
     */
    private int timeoutLimit = 30;  
}
