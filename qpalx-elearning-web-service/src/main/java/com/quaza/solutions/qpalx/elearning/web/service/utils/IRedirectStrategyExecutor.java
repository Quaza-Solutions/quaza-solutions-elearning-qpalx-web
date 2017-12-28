package com.quaza.solutions.qpalx.elearning.web.service.utils;

import com.quaza.solutions.qpalx.elearning.web.service.enums.WebOperationErrorAttributesE;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author manyce400
 */
public interface IRedirectStrategyExecutor {

    public void addWebOperationRedirectErrorsToModel(Model model, HttpServletRequest request);

    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url);

    public void sendRedirectWithError(String targetURL, String errorMessage, WebOperationErrorAttributesE webOperationErrorAttributesE, HttpServletRequest request, HttpServletResponse response);

    public void sendRedirectWithObject(String targetURL, Object sessionObject, WebOperationErrorAttributesE webOperationErrorAttributesE, HttpServletRequest request, HttpServletResponse response);

}
