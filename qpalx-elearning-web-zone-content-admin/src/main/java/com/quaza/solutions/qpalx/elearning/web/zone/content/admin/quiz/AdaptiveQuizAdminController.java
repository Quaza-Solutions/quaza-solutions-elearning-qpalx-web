package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.quiz.IAdaptiveLearningQuizAdminService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IContentAdminTutorialGradePanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.WebOperationErrorAttributesE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
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
import org.springframework.web.multipart.MultipartFile;

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
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService")
    private IELearningStaticContentService ieLearningStaticContentService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.admin.AdaptiveLearningQuizAdminService")
    private IAdaptiveLearningQuizAdminService iAdaptiveLearningQuizAdminService;

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
    public String addQuizQuestionType(Model model, ModelMap modelMap,
                                      @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                      @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
                                      @RequestParam("quizQuestionType") String quizQuestionType,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Adding Quiz question type: {}", quizQuestionType);

        // IF this is a result of a redirect add any web operations errrors to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        // Look up quiz question type
        AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE = AdaptiveLearningQuizQuestionTypeE.getByName(quizQuestionType);

        LOGGER.info("Constructed AdaptiveLearningQuizQuestionVO object...");
        AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO = new AdaptiveLearningQuizQuestionVO();
        adaptiveLearningQuizQuestionVO.setAdaptiveLearningQuizQuestionTypeE(adaptiveLearningQuizQuestionTypeE);

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
        adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(qPalXEMicroLesson);


        LOGGER.info("Quiz Question Answers: {}", adaptiveLearningQuizQuestionVO.getIAdaptiveLearningQuizQuestionAnswerVOs());

        // Get the list of all questions and answers as a map to return for display.
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);


        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
    }

    @RequestMapping(value = "/add-quiz-question-image", method = RequestMethod.POST)
    public String addQuizQuestionTypeImage(Model model, ModelMap modelMap,
                                         @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                         @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
                                         @ModelAttribute("AdaptiveLearningQuizQuestionVO") AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO,
                                         @RequestParam("file") MultipartFile multipartFile,
                                         HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Adding Image Quiz question details to model for adaptiveLearningQuizQuestionVO: {}", adaptiveLearningQuizQuestionVO);

        adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(qPalXEMicroLesson);

        // Upload the image file associated with quiz question
        // Upload file and create the ELearningMediaContent
        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, adaptiveLearningQuizQuestionVO);
        adaptiveLearningQuizQuestionVO.setQuizQuestionAnswerMultiMedia(eLearningMediaContent);

        if(eLearningMediaContent == null) {
            LOGGER.warn("Selected ELearning Media content could not be uploaded.  Check selected file content.");
            String targetURL = "/add-quiz-question-type?quizQuestionType=" + adaptiveLearningQuizQuestionVO.getQuizQuestionTypeString();
            String errorMessage = "Failed to upload file: Check the contents of the file";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else if(eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Uploaded course activity media content file is currently not supported...");
            String targetURL = "/add-quiz-question-type?quizQuestionType=" + adaptiveLearningQuizQuestionVO.getQuizQuestionTypeString();
            String errorMessage = "Uploaded file is not supported: Only Files of type(JPEG, PNG) supported";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else {
            // Build and add all question and provided answers to Quiz
            adaptiveLearningQuizQuestionVO.buildAndAddQuestionAnswerModel();
            adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
            LOGGER.info("Quiz Question Answers: {}", adaptiveLearningQuizQuestionVO.getIAdaptiveLearningQuizQuestionAnswerVOs());

            // Get the list of all questions and answers as a map to return for display.
            Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
            model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);
            return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
        }

        return null;
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

    @RequestMapping(value = "/delete-adaptive-learning-quiz", method = RequestMethod.GET)
    public void deleteAdaptiveLearningQuiz(@RequestParam("adaptiveQuizID") String adaptiveQuizID, Model model,
                                           HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Deleting AdaptiveLearning Quiz with ID: {}", adaptiveQuizID);
        Long id = NumberUtils.toLong(adaptiveQuizID);
        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(id);
        QPalXEMicroLesson qPalXEMicroLesson = adaptiveLearningQuiz.getQPalXEMicroLesson();

        // First delete actual quiz and if that is succesful then delete all Media content for quiz questions
        iAdaptiveLearningQuizAdminService.deleteAdaptiveLearningQuiz(adaptiveLearningQuiz);

        // Redirect to view all quizzes for micro lesson
        String targetURL = "/view-adaptive-quizzes?microlessonID=" + qPalXEMicroLesson.getId();
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

}
