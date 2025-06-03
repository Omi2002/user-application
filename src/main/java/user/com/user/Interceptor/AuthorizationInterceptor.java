package user.com.user.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import user.com.user.dto.SessionUser;

import user.com.user.utils.authorization.AuthorizedToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    AuthorizedToken authorizedToken;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);
        String uri = request.getRequestURI();
        String method = request.getMethod();
        logger.info("Incoming request: {} {}", method, uri);

        HttpSession session = request.getSession();
        if (session == null || session.getAttribute("currentUser") == null) {

            // Two or 3 different session and their entry handle
            logger.warn("Unauthorized access to {}", uri);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized - Please log in.");
            return false;
        }
        // To get know current sessioned user details
       SessionUser sessionUser = (SessionUser) session.getAttribute("currentUser");
        logger.info("Authenticated user: id={}, email={}", sessionUser.getId(), sessionUser.getEmail());
        return true;
    }
}
