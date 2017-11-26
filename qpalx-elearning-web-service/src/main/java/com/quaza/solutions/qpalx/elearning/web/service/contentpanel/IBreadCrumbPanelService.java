package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface IBreadCrumbPanelService {

    public void addBreadCrumbDetails(Model model, AdaptiveLearningQuiz adaptiveLearningQuiz);

}
