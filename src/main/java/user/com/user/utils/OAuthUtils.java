package user.com.user.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OAuthUtils {
    public static String buildGoogleOAuthUrl(String clientId, String redirectUri, String scope, String state) {
        try {
            return "https://accounts.google.com/o/oauth2/v2/auth" +
                    "?client_id=" + URLEncoder.encode(clientId, "UTF-8") +
                    "&redirect_uri=" + URLEncoder.encode(redirectUri, "UTF-8") +
                    "&response_type=code" +
                    "&scope=" + URLEncoder.encode(scope, "UTF-8") +
                    "&access_type=offline" +
                    "&state=" + URLEncoder.encode(state, "UTF-8") +
                    "&include_granted_scopes=true";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error building Google OAuth URL", e);
        }
    }
}
