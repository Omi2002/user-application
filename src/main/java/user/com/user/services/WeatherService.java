package user.com.user.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import user.com.user.dto.ApiResponse;
import user.com.user.exceptions.CurrentDataException;
import user.com.user.exceptions.DayForcastDataException;
import user.com.user.utils.ResponseMessages;

@Service
public class WeatherService {

    static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Value("${accuweather.api.key}")
    private String apiKey;

    @Value("${accuweather.location.key}")
    private String locationKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<?> fetchCurrentConditions() {
        String url = String.format("http://dataservice.accuweather.com/currentconditions/v1/%s?apikey=%s",
                locationKey, apiKey);
        try {
            logger.info("Calling Current Conditions API: {}", url);
            String response = restTemplate.getForObject(url, String.class);
            logger.info("API Response: {}", response);

            JsonElement jsonElement = JsonParser.parseString(response);
            JsonArray array = jsonElement.getAsJsonArray();
            JsonObject obj = array.get(0).getAsJsonObject();

            String weatherText = obj.get("WeatherText").getAsString();
            double temperature = obj.get("Temperature")
                    .getAsJsonObject().get("Metric")
                    .getAsJsonObject().get("Value").getAsDouble();

            Map<String, Object> result = new HashMap<>();
            result.put("weatherText", weatherText);
            result.put("temperatureCelsius", temperature);

            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.error("Exception while fetching current conditions", e);
            throw new CurrentDataException(ResponseMessages.UNABLE_TO_FETCH);
        }
    }

    public ResponseEntity<?> fetchOneDayForecast() {
        String url = String.format("http://dataservice.accuweather.com/forecasts/v1/daily/1day/%s?apikey=%s",
                locationKey, apiKey);
        try {
            logger.info("Calling 1-Day Forecast API: {}", url);
            String response = restTemplate.getForObject(url, String.class);
            logger.debug("API Response: {}", response);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            String headline = root.get("Headline").get("Text").asText();
            double minTemp = root.get("DailyForecasts").get(0).get("Temperature").get("Minimum").get("Value")
                    .asDouble();
            double maxTemp = root.get("DailyForecasts").get(0).get("Temperature").get("Maximum").get("Value")
                    .asDouble();

            Map<String, Object> result = new HashMap<>();
            result.put("headline", headline);
            result.put("minTemp", minTemp);
            result.put("maxTemp", maxTemp);

            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            logger.error("Exception while fetching forecast", e);
            throw new DayForcastDataException(ResponseMessages.UNABLE_TO_FETCH);
        }
    }
}
