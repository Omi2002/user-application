package user.com.user.controller;

import java.io.IOException;

import user.com.user.dto.SessionUser;
import user.com.user.exceptions.OAuthException;
import user.com.user.services.OAuthService;
import user.com.user.utils.GenericResponse;
import user.com.user.utils.ResponseMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    @Autowired
    OAuthService oAuthService;

    @GetMapping("/connect/url")
    public ResponseEntity<?> loginWithGoogle(HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession();
        SessionUser sessionUser = (SessionUser) session.getAttribute("currentUser");
        Integer userId = sessionUser.getId();

        String url = oAuthService.getGoogleOAuthUrl(userId);
        GenericResponse<String> res = new GenericResponse<>(
                HttpStatus.OK.value(),
                ResponseMessages.SUCCESS_LOGIN,
                url);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/callback")
    public void handleGoogleCallback(HttpServletRequest request, HttpServletResponse response) throws IOException  {

        String queryString = request.getQueryString();
        System.out.println("Query String : " + queryString);

        String code = request.getParameter("code");
        String state = request.getParameter("state");

        System.out.println("Code: " + code);
        System.out.println("State: " + state);

        if (code == null || state == null) {
           throw new OAuthException(ResponseMessages.MISSING_CODE_OR_STATE);
        }
         try {
        oAuthService.handleGoogleOAuthCallback(code, state);
        response.sendRedirect("/oauth-success.html"); 
    } catch (Exception e) {
        response.sendRedirect("/oauth-failure.html");  
    }
    }
}
