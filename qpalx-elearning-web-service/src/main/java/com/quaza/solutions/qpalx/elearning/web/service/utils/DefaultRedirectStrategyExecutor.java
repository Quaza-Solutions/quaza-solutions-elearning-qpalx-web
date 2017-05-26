package com.quaza.solutions.qpalx.elearning.web.service.utils;

import com.quaza.solutions.qpalx.elearning.web.service.enums.WebOperationErrorAttributesE;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author manyce400
 */
@Service(DefaultRedirectStrategyExecutor.BEAN_NAME)
public class DefaultRedirectStrategyExecutor implements IRedirectStrategyExecutor {




    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DefaultRedirectStrategyExecutor.class);


    @Override
    public void addWebOperationRedirectErrorsToModel(Model model, HttpServletRequest request) {
        LOGGER.info("Checking for and adding all previous web operations redirect error messages to current request model...");
        for(WebOperationErrorAttributesE webOperationErrorAttributesE : WebOperationErrorAttributesE.values()) {
            Object operationError = request.getSession().getAttribute(webOperationErrorAttributesE.toString());
            if (operationError != null) {
                LOGGER.info("Adding webOperationErrorAttributesE: {} with value: {} to model", webOperationErrorAttributesE, operationError);
                model.addAttribute(webOperationErrorAttributesE.toString(), operationError);
                request.getSession().removeAttribute(webOperationErrorAttributesE.toString());
            }
        }
    }

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String targetURL) {
        LOGGER.debug("Sending redirect to targetURL: {}", targetURL);

        try {
            redirectStrategy.sendRedirect(request, response, targetURL);
        } catch (IOException e) {
            LOGGER.error("Exception occurred while redirecting to targetURL: {}", targetURL, e);
        }
    }

    @Override
    public void sendRedirectWithError(String targetURL, String errorMessage, WebOperationErrorAttributesE webOperationErrorAttributesE, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.debug("Sending redirect to targetURL: {} with error message: {} and webOperationErrorAttributesE: {}", targetURL, errorMessage, webOperationErrorAttributesE);

        try {
            request.getSession().setAttribute(webOperationErrorAttributesE.toString(), errorMessage);
            redirectStrategy.sendRedirect(request, response, targetURL);
        } catch (IOException e) {
            LOGGER.error("Exception occurred while redirecting to targetURL: {}", targetURL, e);
        }
    }
}
