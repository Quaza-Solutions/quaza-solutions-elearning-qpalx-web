package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.web.service.enums.BreadCrumbeModeE;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(BreadCrumbPanelService.BEAN_NAME)
public class BreadCrumbPanelService implements IBreadCrumbPanelService {



    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.contentpanel.BreadCrumbPanelService";

    public static final String ACTIVE_ITEM = "ACTIVE_ITEM";

    public static final String BREAD_CRUMB_MODE = "BREAD_CRUMB_MODE";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BreadCrumbPanelService.class);

    @Override
    public void addBreadCrumbDetails(Model model, AdaptiveLearningQuiz adaptiveLearningQuiz) {
        Assert.notNull(adaptiveLearningQuiz, "adaptiveLearningQuiz");
        LOGGER.debug("Building bread crumb details for quiz: {}", adaptiveLearningQuiz.getQuizTitle());

        // Initialize the BreadCrumbeModeE
        model.addAttribute(BREAD_CRUMB_MODE, BreadCrumbeModeE.Quiz.toString());

        // Create bread crumb tail information
        QPalXEMicroLesson qPalXEMicroLesson = adaptiveLearningQuiz.getQPalXEMicroLesson();
        QPalXELesson qPalXELesson = qPalXEMicroLesson.getQPalXELesson();
        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        addActiveBreadCrumbTrail(model, adaptiveLearningQuiz);
        addMicroLessonBreadCrumbTrail(model, qPalXEMicroLesson);
        addMicroLessonBreadCrumbTrail(model, qPalXELesson);
        addMicroLessonBreadCrumbTrail(model, eLearningCourse);
        addMicroLessonBreadCrumbTrail(model, eLearningCurriculum);
    }

    private void addActiveBreadCrumbTrail(Model model, IHierarchicalLMSContent activeItem) {
        model.addAttribute(ACTIVE_ITEM, activeItem.getHierarchicalLMSContentName());
    }

    private void addMicroLessonBreadCrumbTrail(Model model, QPalXEMicroLesson qPalXEMicroLesson) {
        model.addAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER, qPalXEMicroLesson);
    }

    private void addMicroLessonBreadCrumbTrail(Model model, QPalXELesson qPalXELesson) {
        model.addAttribute(QPalXELesson.QPalXELesson, qPalXELesson);
    }

    private void addMicroLessonBreadCrumbTrail(Model model, ELearningCourse eLearningCourse) {
        model.addAttribute(ELearningCourse.CLASS_ATTRIBUTE_IDENTIFIER, eLearningCourse);
    }

    private void addMicroLessonBreadCrumbTrail(Model model, ELearningCurriculum eLearningCurriculum) {
        model.addAttribute(ELearningCurriculum.CLASS_ATTRIBUTE_IDENTIFIER, eLearningCurriculum);
    }

}
