package user.com.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import user.com.user.services.WeatherService;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

     final static Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentConditions() {
        logger.info("Fetching current weather conditions...");
        return weatherService.fetchCurrentConditions();
    }

    @GetMapping("/forecast")
    public ResponseEntity<?> getOneDayForecast() {
        logger.info("Fetching 1-day weather forecast...");
        return weatherService.fetchOneDayForecast();
    }
}
