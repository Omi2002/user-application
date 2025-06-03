package user.com.user.module;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import user.com.user.exceptions.OAuthException;

import user.com.user.model.User;
import user.com.user.repository.OAuthRepository;
import user.com.user.utils.Configuration;
import user.com.user.utils.ResponseMessages;

@Component
public class GoogleModule {

    @Autowired
    Configuration configuration;

    @Autowired
    OAuthRepository oAuthRepository;

    /**
     * Builds the Google OAuth2 connect URL.
     */
    public String getConnectUrl(String state, User user) throws UnsupportedEncodingException {
        StringBuilder query = new StringBuilder();
        query.append("client_id=").append(configuration.getGoogleClientId());
        query.append("&redirect_uri=").append(URLEncoder.encode(configuration.getGoogleRedirectUri(), "UTF-8"));
        query.append("&response_type=code");
        query.append("&scope=").append(URLEncoder.encode(configuration.getGoogleScoope(), "UTF-8"));
        query.append("&state=").append(state);
        query.append("&access_type=offline");
        query.append("&include_granted_scopes=true");

        return configuration.getGoogleAutEndpoints() + "?" + query.toString();
    }

    public Map<String, String> getToken(String code) throws ParseException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        StringBuilder bodyBuilder = new StringBuilder();
        bodyBuilder.append("code=").append(URLEncoder.encode(code, StandardCharsets.UTF_8))
                .append("&client_id=")
                .append(URLEncoder.encode(configuration.getGoogleClientId(), StandardCharsets.UTF_8))
                .append("&client_secret=")
                .append(URLEncoder.encode(configuration.getGoogleClientSecret(), StandardCharsets.UTF_8))
                .append("&redirect_uri=")
                .append(URLEncoder.encode(configuration.getGoogleRedirectUri(), StandardCharsets.UTF_8))
                .append("&grant_type=authorization_code");

        HttpEntity<String> request = new HttpEntity<>(bodyBuilder.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        String tokenEndpoint = "https://oauth2.googleapis.com/token";
        Map<String, String> tokenMap = new HashMap<>();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenEndpoint, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(responseBody);

                String accessToken = (String) json.get("access_token");
                String refreshToken = (String) json.get("refresh_token");
                String tokenType = (String) json.get("token_type");
                Long expiresIn = (Long) json.get("expires_in");

                tokenMap.put("access_token", accessToken);
                tokenMap.put("refresh_token", refreshToken);
                tokenMap.put("token_type", tokenType);
                tokenMap.put("expires_in", String.valueOf(expiresIn));

            }

        } catch (OAuthException e) {
            throw new OAuthException(ResponseMessages.FETHING_TOKEN);
        }
        return tokenMap;
    }
}
