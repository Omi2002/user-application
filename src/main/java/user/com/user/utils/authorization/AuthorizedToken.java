package user.com.user.utils.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import user.com.user.config.AppConfig;
@Component
public class AuthorizedToken {
    @Autowired
    AppConfig appConfig;
    //Class created to check the authorization 
    public  boolean isAuthorized(String token) {

        System.out.println("Token :" + token + "Auth Token" + appConfig.getAuthToken());
        //Compares the token and application.properties auth value 
        boolean r = token != null && token.equals(appConfig.getAuthToken());
        System.out.println("The value of r:" + r);
        return r; 
    }
}
