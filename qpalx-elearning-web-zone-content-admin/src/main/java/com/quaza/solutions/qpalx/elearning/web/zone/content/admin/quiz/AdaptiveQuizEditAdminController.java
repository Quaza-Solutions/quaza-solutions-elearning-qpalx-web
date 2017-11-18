package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.quiz.IAdaptiveLearningQuizAdminService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.AcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IContentAdminTutorialGradePanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"SelectedQPalXMicroLesson", "AdaptiveLearningQuiz", "AdminAdaptiveLearningQuizWebVO"})
public class AdaptiveQuizEditAdminController {




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
    @Qualifier(AcademicLevelAdminPanelService.BEAN_NAME)
    private IAcademicLevelAdminPanelService iAcademicLevelAdminPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdaptiveQuizEditAdminController.class);




    @RequestMapping(value = "/edit-adaptive-learning-quiz", method = RequestMethod.GET)
    public String addAdaptiveQuiz(Model model, ModelMap modelMap, @RequestParam("adaptiveQuizID") Long adaptiveQuizID) {
        LOGGER.info("Processing request to edit Adaptive Quiz with ID: {}", adaptiveQuizID);

        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(adaptiveQuizID);
        QPalXEMicroLesson qPalXEMicroLesson = adaptiveLearningQuiz.getQPalXEMicroLesson();
        ELearningCourse eLearningCourse = qPalXEMicroLesson.getQPalXELesson().geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        // Register AdaptiveLearningQuizWebVO on modelMap to enable session tracking for all quiz pages
        AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO = new AdaptiveLearningQuizWebVO(adaptiveLearningQuiz);
        modelMap.addAttribute("AdaptiveLearningQuiz", adaptiveLearningQuiz);
        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.AdminAdaptiveLearningQuizWebVO.toString(), adaptiveLearningQuizWebVO);

        // Enable displaying of User overview panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        modelMap.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXMicroLesson.toString(), qPalXEMicroLesson);
        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("edit-adaptive-quiz-details");
    }

    @RequestMapping(value = "/edit-quiz-question-details", method = RequestMethod.POST)
    public String editQuizQestionDetails(Model model, ModelMap modelMap,
                                         @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                         @ModelAttribute("AdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
                                         @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO) {
        LOGGER.info("Attempting to save Adaptive Quiz details for adaptiveLearningQuizWebVO: {}", adaptiveLearningQuizWebVO);

        // Get the list of all questions and answers as a map to return for display.
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);

        ELearningCourse eLearningCourse = qPalXEMicroLesson.getQPalXELesson().geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("edit-customize-adaptive-quiz");
    }

    @RequestMapping(value = "/edit-quiz-question-type", method = RequestMethod.GET)
    public String addQuizQuestionType(Model model, ModelMap modelMap,
                                      @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                      @ModelAttribute("AdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
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

        ELearningCourse eLearningCourse = qPalXEMicroLesson.getQPalXELesson().geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz-question-type");
    }

//    @RequestMapping(value = "/quiz-question-edit", method = RequestMethod.GET)
//    public String editQuizQuestionType(Model model, ModelMap modelMap,
//                                       @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
//                                       @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
//                                       @RequestParam("id") String id,
//                                      HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Edit Quiz Question view requested for Question with ID: {}", id);
//
//        // Find the Quiz that matches this Question id
//        Long quizQuestionID  = NumberUtils.toLong(id);
//        IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOByID(quizQuestionID);
//        LOGGER.info("Found Quiz Question for: {}", iAdaptiveLearningQuizQuestionVO);
//        model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), iAdaptiveLearningQuizQuestionVO);
//
//        // IF this is an image or video question then load the multimedia file for viewing.
//        ELearningMediaContent eLearningMediaContent = iAdaptiveLearningQuizQuestionVO.getQuizQuestionMultiMedia();
//        model.addAttribute(AdaptiveLearningQuizAttributeE.QuizQuestionMedia.toString(), eLearningMediaContent);
//
//        // IF this is a result of a redirect add any web operations errrors to model
//        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);
//
//        ELearningCourse eLearningCourse = qPalXEMicroLesson.getQPalXELesson().geteLearningCourse();
//        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
//
//        // Add Admin AcademicLevel Panel data
//        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());
//
//        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("edit-customize-adaptive-quiz-question-type");
//    }

//    @RequestMapping(value = "/add-quiz-question-details", method = RequestMethod.POST)
//    public String addQuizQuestionDetails(Model model, ModelMap modelMap, @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
//                                         @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
//                                         @ModelAttribute("AdaptiveLearningQuizQuestionVO") AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO,
//                                         HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Adding Quiz question details to model for adaptiveLearningQuizQuestionVO: {}", adaptiveLearningQuizQuestionVO);
//
//
//        // Build and add all question and provided answers to Quiz
//        adaptiveLearningQuizQuestionVO.buildAndAddQuestionAnswerModel();
//
//        // Validate the question details, IF anything is missing redirect back to Quiz Question page
//        Optional<String> optionalValidationMessage = adaptiveLearningQuizQuestionVO.getValidationMessage();
//        if(optionalValidationMessage.isPresent()) {
//            LOGGER.warn("Found Validation Issue: {}, redirecting back to Question entry...", optionalValidationMessage.get());
//            model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), adaptiveLearningQuizQuestionVO);
//            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), optionalValidationMessage.get());
//            //iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
//            return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz-question-type");
//        }
//
//        if (adaptiveLearningQuizQuestionVO.getID() == null) {
//            LOGGER.info("Saving new QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
//            int questionOrder = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs().size() + 1;
//            adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(qPalXEMicroLesson);
//            adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
//            iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, questionOrder);
//        } else {
//            LOGGER.info("Updating previously saved QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
//            iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, 0);
//            adaptiveLearningQuizWebVO.replaceAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
//        }
//
//        // Get the list of all questions and answers as a map to return for display.
//        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
//        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);
//
//        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
//    }
//
//    @RequestMapping(value = "/add-quiz-question-image", method = RequestMethod.POST)
//    public String addQuizQuestionTypeImage(Model model, ModelMap modelMap,
//                                         @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
//                                         @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
//                                         @ModelAttribute("AdaptiveLearningQuizQuestionVO") AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO,
//                                         @RequestParam("file") MultipartFile multipartFile,
//                                         HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Adding Image Quiz question details to model for adaptiveLearningQuizQuestionVO: {}", adaptiveLearningQuizQuestionVO);
//
//        // Build and add all question and provided answers to Quiz
//        adaptiveLearningQuizQuestionVO.buildAndAddQuestionAnswerModel();
//
//        // Validate the question details, IF anything is missing redirect back to Quiz Question page
//        Optional<String> optionalValidationMessage = adaptiveLearningQuizQuestionVO.getValidationMessage();
//        if(optionalValidationMessage.isPresent()) {
//            LOGGER.warn("Found Validation Issue: {}, redirecting back to Question entry...", optionalValidationMessage.get());
//            model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), adaptiveLearningQuizQuestionVO);
//            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), optionalValidationMessage.get());
//            return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz-question-type");
//        }
//
//        adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(qPalXEMicroLesson);
//
//        // Upload the image file associated with quiz question
//        // Upload file and create the ELearningMediaContent
//        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, adaptiveLearningQuizQuestionVO);
//        adaptiveLearningQuizQuestionVO.setQuizQuestionAnswerMultiMedia(eLearningMediaContent);
//
//        if(eLearningMediaContent == null) {
//            LOGGER.warn("Selected ELearning Media content could not be uploaded.  Check selected file content.");
//            String targetURL = "/add-quiz-question-type?quizQuestionType=" + adaptiveLearningQuizQuestionVO.getQuizQuestionTypeString();
//            String errorMessage = "Failed to upload file: Check the contents of the file";
//            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
//        } else if(eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
//            LOGGER.warn("Uploaded course activity media content file is currently not supported...");
//            String targetURL = "/add-quiz-question-type?quizQuestionType=" + adaptiveLearningQuizQuestionVO.getQuizQuestionTypeString();
//            String errorMessage = "Uploaded file is not supported: Only Files of type(JPEG, PNG) supported";
//            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
//        } else {
//            if (adaptiveLearningQuizQuestionVO.getID() == null) {
//                LOGGER.info("Saving new QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
//                int questionOrder = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs().size() + 1;
//                adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(qPalXEMicroLesson);
//                adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
//                iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, questionOrder);
//            } else {
//                LOGGER.info("Updating previously saved QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
//                iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, 0);
//                adaptiveLearningQuizWebVO.replaceAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
//            }
//
//            // Get the list of all questions and answers as a map to return for display.
//            Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
//            model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);
//            return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
//        }
//
//        return null;
//    }
//
//    @RequestMapping(value = "/persist-adaptive-quiz", method = RequestMethod.POST)
//    public void saveEntireAdaptiveLearningQuiz(SessionStatus status, @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
//                                               @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO, HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Saving and persisting entire adaptive learning quiz: {}", adaptiveLearningQuizWebVO);
//
//        iAdaptiveLearningQuizService.updateFrom(adaptiveLearningQuizWebVO);
//        status.setComplete();
//
//        // Load the correct micro lesson to return back to
//        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(adaptiveLearningQuizWebVO.getID());
//        QPalXEMicroLesson qPalXEMicroLesson1 = adaptiveLearningQuiz.getQPalXEMicroLesson();
//
//        String targetURL = "/view-adaptive-quizzes?microlessonID=" + qPalXEMicroLesson1.getId();
//        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
//    }
//
//    @RequestMapping(value = "/edit-adaptive-learning-quiz", method = RequestMethod.GET)
//    public String editAdaptiveLearningQuiz(@RequestParam("adaptiveQuizID") String adaptiveQuizID,
//                                           Model model, ModelMap modelMap,
//                                           HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Edit request received for AdaptiveLearning Quiz with ID: {}", adaptiveQuizID);
//        Long id = NumberUtils.toLong(adaptiveQuizID);
//        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(id);
//        QPalXEMicroLesson qPalXEMicroLesson = adaptiveLearningQuiz.getQPalXEMicroLesson();
//
//        // Create Value Objects for use across web pages
//        AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO = new AdaptiveLearningQuizWebVO(adaptiveLearningQuiz);
//
//        // Enable displaying of User overview panel
//        qPalXUserInfoPanelService.addUserInfoAttributes(model);
//        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());
//
//        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.AdminAdaptiveLearningQuizWebVO.toString(), adaptiveLearningQuizWebVO);
//        modelMap.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXMicroLesson.toString(), qPalXEMicroLesson);
//        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("add-adaptive-quiz-details");
//    }
//
//    @RequestMapping(value = "/delete-adaptive-learning-quiz", method = RequestMethod.GET)
//    public void deleteAdaptiveLearningQuiz(@RequestParam("adaptiveQuizID") String adaptiveQuizID,
//                                           HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Deleting AdaptiveLearning Quiz with ID: {}", adaptiveQuizID);
//        Long id = NumberUtils.toLong(adaptiveQuizID);
//        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(id);
//        QPalXEMicroLesson qPalXEMicroLesson = adaptiveLearningQuiz.getQPalXEMicroLesson();
//
//        // First delete actual quiz and if that is succesful then delete all Media content for quiz questions
//        iAdaptiveLearningQuizAdminService.deleteAdaptiveLearningQuiz(adaptiveLearningQuiz);
//
//        // Redirect to view all quizzes for micro lesson
//        String targetURL = "/view-adaptive-quizzes?microlessonID=" + qPalXEMicroLesson.getId();
//        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
//    }

}
