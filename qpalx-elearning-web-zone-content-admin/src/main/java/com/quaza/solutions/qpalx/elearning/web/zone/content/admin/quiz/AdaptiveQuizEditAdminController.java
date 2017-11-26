package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.quiz.IAdaptiveLearningQuizAdminService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.*;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.quiz.editor.ClassicQuizEditor;
import com.quaza.solutions.qpalx.elearning.web.service.quiz.editor.IClassicQuizEditor;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.EditableAdaptiveLearningQuizQuestionVO;
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
import java.util.Set;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {
        QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER, AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER, AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE, AdaptiveLearningQuizQuestion.CLASS_ATTRIBUTE_IDENTIFIER
})
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
    @Qualifier(BreadCrumbPanelService.BEAN_NAME)
    private IBreadCrumbPanelService iBreadCrumbPanelService;

    @Autowired
    @Qualifier(ClassicQuizEditor.BEAN_NAME)
    private IClassicQuizEditor iClassicQuizEditor;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdaptiveQuizEditAdminController.class);




    @RequestMapping(value = "/start-quiz-edit-mode", method = RequestMethod.GET)
    public String addAdaptiveQuiz(Model model, ModelMap modelMap, @RequestParam("adaptiveQuizID") Long adaptiveQuizID) {
        LOGGER.info("Processing request to edit Adaptive Quiz with ID: {}", adaptiveQuizID);

        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(adaptiveQuizID);
        QPalXEMicroLesson qPalXEMicroLesson = adaptiveLearningQuiz.getQPalXEMicroLesson();
        ELearningCourse eLearningCourse = qPalXEMicroLesson.getQPalXELesson().geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        // Register AdaptiveLearningQuizWebVO on modelMap to enable session tracking for all quiz pages
        AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO = new AdaptiveLearningQuizWebVO(adaptiveLearningQuiz);
        modelMap.addAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER, adaptiveLearningQuiz);
        modelMap.addAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE, adaptiveLearningQuizWebVO);
        modelMap.addAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER, qPalXEMicroLesson);

        // Enable displaying of User overview panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());
        return ContentRootE.Content_Admin_Quiz_Edit.getContentRootPagePath("start-adaptive-quiz-edit");
    }


    @RequestMapping(value = "/refresh-quiz-for-customization", method = RequestMethod.GET)
    public String refreshQuizDetailsForCustomizationView(Model model, ModelMap modelMap, @ModelAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuiz adaptiveLearningQuiz) {
        LOGGER.info("Refreshing quiz for customization view");
        AdaptiveLearningQuiz refreshedQuizInstance = iAdaptiveLearningQuizService.findByID(adaptiveLearningQuiz.getId());
        AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO = new AdaptiveLearningQuizWebVO(refreshedQuizInstance);
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();

        modelMap.addAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER, refreshedQuizInstance);
        modelMap.addAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE, adaptiveLearningQuizWebVO);
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);

        // Add bread crumbs information
        iBreadCrumbPanelService.addBreadCrumbDetails(model, refreshedQuizInstance);

        return ContentRootE.Content_Admin_Quiz_Edit.getContentRootPagePath("customize-quiz-question-details");
    }


    @RequestMapping(value = "/update-basic-quiz-info", method = RequestMethod.POST)
    public void executeQuizQuestionInfoRefresh(Model model,
                                               HttpServletRequest httpServletRequest,
                                               HttpServletResponse httpServletResponse,
                                               @ModelAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuiz adaptiveLearningQuiz,
                                               @ModelAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE) AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO) {
        LOGGER.info("Attempting to update Quiz details with edit");
        iClassicQuizEditor.updateQuizWithEdits(adaptiveLearningQuiz, adaptiveLearningQuizWebVO);
        iRedirectStrategyExecutor.sendRedirect(httpServletRequest, httpServletResponse, "/refresh-quiz-for-customization");
    }


    @RequestMapping(value = "/quiz-question-update-view", method = RequestMethod.GET)
    public String executeQuizQuestionView(Model model,
                                          ModelMap modelMap,
                                          HttpServletRequest request,
                                          @RequestParam("id") Long quizQuestionID,
                                          @ModelAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuiz adaptiveLearningQuiz) {
        LOGGER.info("Edit view requested for Quiz Question with ID: {}", quizQuestionID);

        // Find the Quiz that matches this Question id
        AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion = iAdaptiveLearningQuizService.findByQuizQuestionID(quizQuestionID);
        EditableAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO = new EditableAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestion);

        LOGGER.info("Found Quiz Question for: {}", iAdaptiveLearningQuizQuestionVO.getQuestionTitle());
        model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), iAdaptiveLearningQuizQuestionVO);
        modelMap.addAttribute(AdaptiveLearningQuizQuestion.CLASS_ATTRIBUTE_IDENTIFIER, adaptiveLearningQuizQuestion);
        modelMap.addAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER, adaptiveLearningQuizQuestion.getAdaptiveLearningQuiz());

        // IF this is a result of a redirect add any web operations errrors to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        ELearningCourse eLearningCourse = adaptiveLearningQuiz.getQPalXEMicroLesson().getQPalXELesson().geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        // Add bread crumbs information
        iBreadCrumbPanelService.addBreadCrumbDetails(model, adaptiveLearningQuiz);

        return ContentRootE.Content_Admin_Quiz_Edit.getContentRootPagePath("modify-quiz-question-by-type");
    }

    @RequestMapping(value = "/quiz-question-media-update-view", method = RequestMethod.GET)
    public String executeQuizQuestionMediaView(Model model,
                                               ModelMap modelMap,
                                               @RequestParam("id") Long quizQuestionID,
                                               @ModelAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuiz adaptiveLearningQuiz) {
        LOGGER.info("Edit view requested for Quiz Question with ID: {}", quizQuestionID);

        // Find the Quiz that matches this Question id
        AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion = iAdaptiveLearningQuizService.findByQuizQuestionID(quizQuestionID);
        EditableAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO = new EditableAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestion);

        LOGGER.info("Found Quiz Question for: {}", iAdaptiveLearningQuizQuestionVO.getQuestionTitle());
        model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), iAdaptiveLearningQuizQuestionVO);
        modelMap.addAttribute(AdaptiveLearningQuizQuestion.CLASS_ATTRIBUTE_IDENTIFIER, adaptiveLearningQuizQuestion);
        modelMap.addAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER, adaptiveLearningQuizQuestion.getAdaptiveLearningQuiz());

        // Add bread crumbs information
        iBreadCrumbPanelService.addBreadCrumbDetails(model, adaptiveLearningQuiz);
        return ContentRootE.Content_Admin_Quiz_Edit.getContentRootPagePath("question-media-edit");
    }

    @RequestMapping(value = "/submit-question-edit-update", method = RequestMethod.POST)
    public void executeQuestionModification(Model model, ModelMap modelMap,
                                            HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse,
                                            @ModelAttribute(AdaptiveLearningQuizQuestion.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion,
                                            @ModelAttribute("AdaptiveLearningQuizQuestionVO") EditableAdaptiveLearningQuizQuestionVO editableAdaptiveLearningQuizQuestionVO) {
        LOGGER.info("Quiz Question update has been requested for question with ID: {}", adaptiveLearningQuizQuestion.getId());
        iClassicQuizEditor.updateQuizQuestionWithEdits(adaptiveLearningQuizQuestion, editableAdaptiveLearningQuizQuestionVO);
        iRedirectStrategyExecutor.sendRedirect(httpServletRequest, httpServletResponse, "/refresh-quiz-for-customization");
    }



    @RequestMapping(value = "/submit-question-edit-update-image", method = RequestMethod.POST)
    public void executeQuestionModificationImage(Model model, ModelMap modelMap,
                                                 HttpServletRequest httpServletRequest,
                                                 HttpServletResponse httpServletResponse,
                                                 @RequestParam("file") MultipartFile multipartFile,
                                                 @ModelAttribute(AdaptiveLearningQuizQuestion.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion) {
        LOGGER.info("Quiz Question update has been requested for question with ID: {}", adaptiveLearningQuizQuestion.getId());
        EditableAdaptiveLearningQuizQuestionVO editableAdaptiveLearningQuizQuestionVO = new EditableAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestion);
        editableAdaptiveLearningQuizQuestionVO.setiHierarchicalLMSContent(adaptiveLearningQuizQuestion.getAdaptiveLearningQuiz());
        editableAdaptiveLearningQuizQuestionVO.setQPalXTutorialContentType(QPalXTutorialContentTypeE.Quiz.toString());
        iClassicQuizEditor.updateQuizQuestionWithEdits(adaptiveLearningQuizQuestion, editableAdaptiveLearningQuizQuestionVO, multipartFile);
        iRedirectStrategyExecutor.sendRedirect(httpServletRequest, httpServletResponse, "/refresh-quiz-for-customization");
    }



    //    @RequestMapping(value = "/edit-quiz-question-type", method = RequestMethod.GET)
//    public String editQuestionByType(Model model, ModelMap modelMap,
//                                      @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
//                                      @ModelAttribute("AdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
//                                      @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
//                                      @RequestParam("quizQuestionType") String quizQuestionType,
//                                      HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Adding Quiz question type: {}", quizQuestionType);
//
//        // IF this is a result of a redirect add any web operations errrors to model
//        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);
//
//        // Look up quiz question type
//        AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE = AdaptiveLearningQuizQuestionTypeE.getByName(quizQuestionType);
//
//        LOGGER.info("Constructed AdaptiveLearningQuizQuestionVO object...");
//        AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO = new AdaptiveLearningQuizQuestionVO();
//        adaptiveLearningQuizQuestionVO.setAdaptiveLearningQuizQuestionTypeE(adaptiveLearningQuizQuestionTypeE);
//
//        model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), adaptiveLearningQuizQuestionVO);
//
//        ELearningCourse eLearningCourse = qPalXEMicroLesson.getQPalXELesson().geteLearningCourse();
//        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
//
//        // Add Admin AcademicLevel Panel data
//        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());
//
//        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("edit-customize-adaptive-quiz-question-type");
//    }


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


    @RequestMapping(value = "/exit-quiz-edit-mode", method = RequestMethod.POST)
    public void exitQuizEditMode(SessionStatus status,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 @ModelAttribute(AdaptiveLearningQuiz.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuiz adaptiveLearningQuiz) {
        LOGGER.info("Exiting Quiz edit mode for quiz with ID: {}", adaptiveLearningQuiz.getId());
        status.setComplete();
        QPalXEMicroLesson qPalXEMicroLesson1 = adaptiveLearningQuiz.getQPalXEMicroLesson();
        String targetURL = "/view-adaptive-quizzes?microlessonID=" + qPalXEMicroLesson1.getId();
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

}
