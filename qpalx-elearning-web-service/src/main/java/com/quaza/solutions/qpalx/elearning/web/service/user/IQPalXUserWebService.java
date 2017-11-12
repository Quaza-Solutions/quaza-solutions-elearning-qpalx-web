package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.web.security.login.WebQPalXUser;
import org.springframework.ui.Model;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IQPalXUserWebService {


    // Finds and returns the currently logged in QPalXUser session.  IF no user logged in returns Optional#absent
    public Optional<QPalXUser> getLoggedInQPalXUser();

    // Given a QPalxUser Email address execute auto login, return false if auto login failed.
    public boolean executeAutoLogin(String userEmail);


    // Get and return the WebQPalXUser of currently logged in user
    public WebQPalXUser getWebQPalXUser();

    // Add basic QPalXUser info details
    public void addQPalXUserInfoDetailsToWebModel(final Model model, QPalXUser qPalXUser);


    // Loads and adds all the ELearning curriculum by CurriculumType based on the UserType of qPalXUser
    public void addQPalXUserAccesibleCurriculum(final Model model, QPalXUser qPalXUser);

}
