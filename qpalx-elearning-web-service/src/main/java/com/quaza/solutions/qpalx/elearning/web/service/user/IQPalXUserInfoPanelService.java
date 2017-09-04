package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface IQPalXUserInfoPanelService {

    public void addUserInfoAttributes(Model model);

    public void addStudentUserInfoAttributes(Model model, QPalXUser qPalXUser);

}
