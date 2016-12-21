package com.quaza.solutions.qpalx.elearning.web.security.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 *
 * @author manyce400
 */
@Service
public class DefaultQPalXLogoutHandler implements LogoutHandler {


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultQPalXLogoutHandler.class);

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        WebQPalXUser webQPalXUser = (WebQPalXUser) authentication.getPrincipal();
        LOGGER.info("Logging out user with email: {} ...", webQPalXUser.getEmail());

        try {
            httpServletRequest.logout();
            httpServletRequest.getSession().invalidate();
            LOGGER.info("User logout completed.");
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}

