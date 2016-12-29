package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IContentAdminTutorialGradePanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"SelectedQPalXMicroLesson", "AdminAdaptiveLearningQuizWebVO"})
public class AdaptiveQuizAdminController {




    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXEMicroLessonService")
    private IQPalXEMicroLessonService iqPalXEMicroLessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizService2")
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.web.ContentAdminTutorialGradePanelService")
    private IContentAdminTutorialGradePanelService iContentAdminTutorialGradePanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdaptiveQuizAdminController.class);



    @RequestMapping(value = "/view-adaptive-quizzes", method = RequestMethod.GET)
    public String viewAdminAdaptiveQuizzes(final Model model, @RequestParam("microlessonID") String microlessonID) {
        LOGGER.info("Finding all Adaptive Quizzes for microlessonID: {}", microlessonID);

        // Enable displaying of User overview panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        Long id = NumberUtils.toLong(microlessonID);
        QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(id);
        QPalXELesson qPalXELesson = qPalXEMicroLesson.getQPalXELesson();
        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXMicroLesson.toString(), qPalXEMicroLesson);

        List<AdaptiveLearningQuiz> adaptiveLearningQuizList = iAdaptiveLearningQuizService.findQuizzesForMicroLesson(qPalXEMicroLesson);
        model.addAttribute(AdaptiveLearningQuizAttributeE.AdminMicroLessonAdaptiveQuizzes.toString(), adaptiveLearningQuizList);

        iContentAdminTutorialGradePanelService.addDisplayPanelAttributes(model, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, qPalXELesson);

        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("view-microlesson-quizzes");
    }


    @RequestMapping(value = "/create-qpalx-quiz", method = RequestMethod.GET)
    public String addAdaptiveQuiz(Model model, ModelMap modelMap, @RequestParam("microLessonID") String microLessonID) {
        LOGGER.info("Processing request to add Adaptive Quiz for microLessonID: {}", microLessonID);

        // Enable displaying of User overview panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Register AdaptiveLearningQuizWebVO on modelMap to enable session tracking for all quiz pages
        AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO = new AdaptiveLearningQuizWebVO();
        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.AdminAdaptiveLearningQuizWebVO.toString(), adaptiveLearningQuizWebVO);

        Long id = NumberUtils.toLong(microLessonID);
        QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(id);
        modelMap.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXMicroLesson.toString(), qPalXEMicroLesson);
        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("add-adaptive-quiz-details");
    }

    @RequestMapping(value = "/process-quiz-details", method = RequestMethod.POST)
    public String saveAdaptiveQuizDetails(Model model, ModelMap modelMap, @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                          @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO) {
        LOGGER.info("Attempting to save Adaptive Quiz details for adaptiveLearningQuizWebVO: {}", adaptiveLearningQuizWebVO);

        // Get the list of all questions and answers as a map to return for display.
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);

        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
    }

    @RequestMapping(value = "/add-quiz-question-type", method = RequestMethod.GET)
    public String addQuizQuestionType(Model model, ModelMap modelMap, @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                      @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO, @RequestParam("quizQuestionType") String quizQuestionType) {
        LOGGER.info("Adding Quiz question type: {}", quizQuestionType);

        AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO = new AdaptiveLearningQuizQuestionVO();
        adaptiveLearningQuizQuestionVO.setAdaptiveLearningQuizQuestionTypeE(AdaptiveLearningQuizQuestionTypeE.Multiple_Choice);
        model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), adaptiveLearningQuizQuestionVO);

        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz-question-type");
    }

    @RequestMapping(value = "/add-quiz-question-details", method = RequestMethod.POST)
    public String addQuizQuestionDetails(Model model, ModelMap modelMap, @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                         @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
                                         @ModelAttribute("AdaptiveLearningQuizQuestionVO") AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO) {
        LOGGER.info("Adding Quiz question details to model for adaptiveLearningQuizQuestionVO: {}", adaptiveLearningQuizQuestionVO);

        // Build and add all question and provided answers to Quiz
        adaptiveLearningQuizQuestionVO.buildAndAddQuestionAnswerModel();
        adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);


        LOGGER.info("Quiz Question Answers: {}", adaptiveLearningQuizQuestionVO.getIAdaptiveLearningQuizQuestionAnswerVOs());

        // Get the list of all questions and answers as a map to return for display.
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);


        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
    }

    @RequestMapping(value = "/persist-adaptive-quiz", method = RequestMethod.POST)
    public void saveEntireAdaptiveLearningQuiz(SessionStatus status, @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                               @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Saving and persisting entire adaptive learning quiz: {}", adaptiveLearningQuizWebVO);

        iAdaptiveLearningQuizService.createAndSaveAdaptiveLearningQuiz(qPalXEMicroLesson, adaptiveLearningQuizWebVO);
        status.setComplete();

        String targetURL = "/view-adaptive-quizzes?microlessonID=" + qPalXEMicroLesson.getId();
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

}
