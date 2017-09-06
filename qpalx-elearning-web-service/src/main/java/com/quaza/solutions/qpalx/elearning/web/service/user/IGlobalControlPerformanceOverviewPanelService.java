package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface IGlobalControlPerformanceOverviewPanelService {


    public void addPerformanceOverviewInCurriculum(Model model, ELearningCurriculum eLearningCurriculum, QPalXUser studentQPalxUser);

}
