package com.quaza.solutions.qpalx.elearning.web.security.login;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

/**
 *
 *
 * @author manyce400
 */
@Service("quaza.solutions.qpalx.elearning.web.DefaultQPalXAuthenticationSuccessFailureHandler")
public class DefaultQPalXAuthenticationSuccessFailureHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {



    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultQPalXAuthenticationSuccessFailureHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        QPalxUserTypeE qPalxUserTypeE = getQPalxUserTypeE(authentication);

        String targetURL = "/";

        switch (qPalxUserTypeE) {
            case STUDENT:
                targetURL = "/";
                break;
            case ADMINISTRATOR:
                targetURL = "/";
                break;
            default:
                break;
        }

        LOGGER.info("QPalX authentication completed succesfully, determining user's landing page");
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, targetURL);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {
        WebQPalXUser authenticationFailedUser = getAuthenticationFailedWebUser(exception);
        httpServletRequest.setAttribute("AuthenticationFailedUser", authenticationFailedUser);
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/qpalx-access-failure");
    }

    private QPalxUserTypeE getQPalxUserTypeE(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Optional<? extends GrantedAuthority> userAuthority = authorities.stream()
                .filter((authority) -> authority.getAuthority().equals(QPalxUserTypeE.STUDENT.toString())
                        || authority.getAuthority().equals(QPalxUserTypeE.CONTENT_DEVELOPER.toString())
                        || authority.getAuthority().equals(QPalxUserTypeE.ADMINISTRATOR.toString()))
                .findFirst();

        if(userAuthority.isPresent()) {
            QPalxUserTypeE actualQPalxUserTypeE =  QPalxUserTypeE.valueOf(userAuthority.get().getAuthority());
            return actualQPalxUserTypeE;
        }

        LOGGER.warn("Could not determine the granted authorities of principal user: {}", principal);
        return null;
    }

    private WebQPalXUser getAuthenticationFailedWebUser(AuthenticationException exception) {
        WebQPalXUser authenticationFailedUser;
        Throwable exceptionCause = exception.getCause();

        if(exceptionCause instanceof BadCredentialsException) {
            // password did not match the password we have on file return empty web user with invalid subscription
            authenticationFailedUser = new WebQPalXUser();
            return authenticationFailedUser;
        } else if(exceptionCause instanceof QPalXUserLoginException) {
            QPalXUserLoginException qPalXUserLoginException = (QPalXUserLoginException) exceptionCause;
            Optional<WebQPalXUser> optional = qPalXUserLoginException.getWebQPalXUser();
            authenticationFailedUser = optional.get();
            return authenticationFailedUser;
        }

        return null;
    }
}
