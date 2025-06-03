package user.com.user.services;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.antlr.v4.runtime.misc.Utils;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import user.com.user.exceptions.OAuthException;
import user.com.user.exceptions.UserNotFoundException;
import user.com.user.handler.GoogleHandler;
import user.com.user.model.OAuth;
import user.com.user.model.User;
import user.com.user.module.GoogleModule;
import user.com.user.repository.OAuthRepository;
import user.com.user.repository.UserRepository;
import user.com.user.utils.ResponseMessages;
import user.com.user.utils.Util;


@Service
public class OAuthService {

    @Autowired
    private GoogleModule googleModule;

    @Autowired
    private GoogleHandler googleHandler;

    @Autowired
    private OAuthRepository oAuthRepository;

    @Autowired
    private UserRepository userRepository;

    public String getGoogleOAuthUrl(Integer userId) throws UnsupportedEncodingException {
        Optional<User> user = userRepository.findById(userId);
        if (user == null || user.isEmpty()) {
            throw new UserNotFoundException(ResponseMessages.NOT_FOUND + userId);
        }

        String state = Util.id32();

        OAuth oauth = new OAuth();
        oauth.setRequestId(state);
        oauth.setUserId(user.get().getId());
        oauth.setStatus(false);
        oauth.setCreatedTime(LocalDateTime.now());
        oauth.setUpdatedTime(LocalDateTime.now());

        oAuthRepository.save(oauth);
        return googleModule.getConnectUrl(state, user.get());
    }

    public void handleGoogleOAuthCallback(String code, String state) throws ParseException {
        
        Optional<OAuth> oauthRecord = oAuthRepository.findByRequestIdAndStatus(state, false);
        System.out.println("Find by Request Id :" + oauthRecord);
        if (oauthRecord == null || oauthRecord.isEmpty()) {
            throw new OAuthException(ResponseMessages.EXPIRED_SESSION);
        }
        OAuth oauth = oauthRecord.get();

        // 2. Exchange code for access token
        Map<String, String> tokenResponse = googleHandler.checkToken(code);
        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            // Will change with Genric response
            throw new OAuthException(ResponseMessages.MISSING_TOKEN);
        }

        // 3. Store access token and update oauth entry
        oauth.setOauthData(tokenResponse.toString());
        oauth.setUpdatedTime(LocalDateTime.now());
        oauth.setStatus(true); 
        oAuthRepository.save(oauth);
    }
}

