package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.web.security.login.WebQPalXUser;
import org.springframework.ui.Model;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IQPalXUserWebService {


    /**
     * Finds and returns the currently logged in QPalXUser session.  IF no user logged in returns Optional#absent
     *
     * @return
     */
    public Optional<QPalXUser> getLoggedInQPalXUser();

    /**
     * @return The full blown WebQPalXUser instance
     */
    public WebQPalXUser getWebQPalXUser();


    /**
     * Add basic QPalXUser info details
     */
    public void addQPalXUserInfoDetailsToWebModel(final Model model, QPalXUser qPalXUser);


    /**
     * Loads and adds all the ELearning curriculum by CurriculumType based on the UserType of qPalXUser
     * @param model
     * @param qPalXUser
     */
    public void addQPalXUserAccesibleCurriculum(final Model model, QPalXUser qPalXUser);

}
