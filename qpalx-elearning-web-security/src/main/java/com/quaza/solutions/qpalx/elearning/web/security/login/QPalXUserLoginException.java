package com.quaza.solutions.qpalx.elearning.web.security.login;

import org.springframework.security.authentication.AccountExpiredException;

import java.util.Optional;

/**
 * Exception that gets thrown when validating a user's access as part of login fails.
 *
 * @author manyce400
 */
public class QPalXUserLoginException extends AccountExpiredException {


    private final WebQPalXUser webQPalXUser;

    public QPalXUserLoginException(String msg) {
        super(msg);
        this.webQPalXUser = null;
    }

    public QPalXUserLoginException(WebQPalXUser webQPalXUser, String msg) {
        super(msg);
        this.webQPalXUser = webQPalXUser;
    }

    public QPalXUserLoginException(WebQPalXUser webQPalXUser, String msg, Throwable t) {
        super(msg, t);
        this.webQPalXUser = webQPalXUser;
    }

    public Optional<WebQPalXUser> getWebQPalXUser() {
        if(webQPalXUser != null) {
            return Optional.of(webQPalXUser);
        }

        return Optional.empty();
    }
}
