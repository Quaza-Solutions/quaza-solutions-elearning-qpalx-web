package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.assessment;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.assessment.CourseAssessmentFocusArea;
import com.quaza.solutions.qpalx.elearning.domain.lms.assessment.CurriculumProficiencyAssessment;
import com.quaza.solutions.qpalx.elearning.domain.lms.assessment.ICourseAssessmentFocusAreaVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.service.lms.assessment.CourseAssessmentFocusAreaService;
import com.quaza.solutions.qpalx.elearning.service.lms.assessment.CurriculumProficiencyAssessmentService;
import com.quaza.solutions.qpalx.elearning.service.lms.assessment.ICourseAssessmentFocusAreaService;
import com.quaza.solutions.qpalx.elearning.service.lms.assessment.ICurriculumProficiencyAssessmentService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.CacheEnabledELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.DefaultELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.assessment.CurriculumAssessmentAdminService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.assessment.ICurriculumAssessmentAdminService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.WebOperationErrorAttributesE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.QPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.DefaultRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.CourseAssessmentFocusAreaVO;
import com.quaza.solutions.qpalx.elearning.web.zone.content.admin.quiz.EmbedableQuizAdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(
        value = {
                EmbedableQuizAdminController.QUIZ_CREATION_COMPLETION_EXIT_URL_ATTR, CurriculumProficiencyAssessment.CLASS_ATTRIBUTE_IDENTIFIER, AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE,
                IHierarchicalLMSContent.CLASS_ATTRIBUTE_IDENTIFIER, ELearningCurriculum.CLASS_ATTRIBUTE_IDENTIFIER
        }
)
public class CurriculumProficiencyAssessmentAdminController {




    @Autowired
    @Qualifier(CacheEnabledELearningCurriculumService.BEAN_NAME)
    private IELearningCurriculumService ieLearningCurriculumService;


    @Autowired
    @Qualifier(DefaultELearningCourseService.BEAN_NAME)
    private IELearningCourseService ieLearningCourseService;



    @Autowired
    @Qualifier(QPalXUserInfoPanelService.BEAN_NAME)
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;



    @Autowired
    @Qualifier(CurriculumProficiencyAssessmentService.SPRING_BEAN)
    private ICurriculumProficiencyAssessmentService iCurriculumProficiencyAssessmentService;


    @Autowired
    @Qualifier(CourseAssessmentFocusAreaService.BEAN_NAME)
    private ICourseAssessmentFocusAreaService iCourseAssessmentFocusAreaService;


    @Autowired
    @Qualifier(CurriculumAssessmentAdminService.BEAN_NAME)
    private ICurriculumAssessmentAdminService iCurriculumAssessmentAdminService;


    @Autowired
    @Qualifier(DefaultRedirectStrategyExecutor.BEAN_NAME)
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CurriculumProficiencyAssessmentAdminController.class);


    @RequestMapping(value = "/add-curriculum-assessment", method = RequestMethod.GET)
    public String addCurriculumAssessment(Model model, ModelMap modelMap, @RequestParam("curriculumID") Long curriculumID) {
        LOGGER.info("Handling request to create new ELearningCurriculum assessment for curriculumID: {}", curriculumID);

        // Enable displaying of User overview panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(curriculumID);
        modelMap.addAttribute(ELearningCurriculum.CLASS_ATTRIBUTE_IDENTIFIER, eLearningCurriculum);
        return ContentRootE.Content_Admin_Curriculum_Assessment.getContentRootPagePath("assessment-admin-launch");
    }



    @RequestMapping(value = "/add-assessment-course-focus-area", method = RequestMethod.GET)
    //@RequestMapping(value = "/Assessment/FocusArea/Add/{focusArea}")
    public String addCurriculumAssessment(Model model,
                                          @ModelAttribute("focusArea") String focusArea,
                                          @ModelAttribute(ELearningCurriculum.CLASS_ATTRIBUTE_IDENTIFIER) ELearningCurriculum eLearningCurriculum) {
        LOGGER.info("Initializing view for curriculum focus area: {}", focusArea);
        List<ELearningCourse> curriculumCourses = ieLearningCourseService.findByELearningCurriculum(eLearningCurriculum);
        CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO = new CourseAssessmentFocusAreaVO();
        model.addAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE, courseAssessmentFocusAreaVO);
        model.addAttribute(CurriculumDisplayAttributeE.AvailableCurriculumCourses.toString(), curriculumCourses);
        return ContentRootE.Content_Admin_Curriculum_Assessment.getContentRootPagePath("add-course-focus-area-details");
    }




    @RequestMapping(value = "/initialize-assessment-focus-area", method = RequestMethod.POST)
    public void initializeCourseAssessmentFocusArea(Model model,
                                                    ModelMap modelMap,
                                                    HttpServletRequest request, HttpServletResponse response,
                                                    @ModelAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE) CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO,
                                                    @ModelAttribute(ELearningCurriculum.CLASS_ATTRIBUTE_IDENTIFIER) ELearningCurriculum eLearningCurriculum) {
        LOGGER.info("Initializing assessment course focus area from: {}", courseAssessmentFocusAreaVO);
        iCurriculumAssessmentAdminService.addCourseAssessmentFocusAreaVO(courseAssessmentFocusAreaVO);

        // Load the selected course and addi it as the parent content. Quiz will be created under this
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(courseAssessmentFocusAreaVO.getELearningCourseID());
        model.addAttribute(IHierarchicalLMSContent.CLASS_ATTRIBUTE_IDENTIFIER, eLearningCourse);

        // Add the AdaptiveLearninQuiz compatible object for EmbedController
        model.addAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE, courseAssessmentFocusAreaVO);

        // Create new course assessment focus area and save details
        Set<ICourseAssessmentFocusAreaVO> courseAssessmentFocusAreaVOS = ImmutableSet.of(courseAssessmentFocusAreaVO);
        CurriculumProficiencyAssessment curriculumProficiencyAssessment = iCurriculumProficiencyAssessmentService.makeCurriculumProficiencyRankingAssessment(eLearningCurriculum, courseAssessmentFocusAreaVOS);
        modelMap.addAttribute(CurriculumProficiencyAssessment.CLASS_ATTRIBUTE_IDENTIFIER, curriculumProficiencyAssessment);

        // Add URL that should be redirected back to when the Embedded Quiz Controller is complete
        modelMap.addAttribute(EmbedableQuizAdminController.QUIZ_CREATION_COMPLETION_EXIT_URL_ATTR, "/update-assessment-focus-area-details");
        iRedirectStrategyExecutor.sendRedirect(request, response, "launch-embedded-content-quiz-mode");
    }




    @RequestMapping(value = "/update-assessment-focus-area-details", method = RequestMethod.GET)
    public String updateCourseAssessmentFocusAreaQuizDetails(Model model,
                                                           @ModelAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE) CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO,
                                                           @ModelAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE) AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
                                                           @ModelAttribute(CurriculumProficiencyAssessment.CLASS_ATTRIBUTE_IDENTIFIER) CurriculumProficiencyAssessment curriculumProficiencyAssessment) {
        LOGGER.info("Updating Assessment information with newly created course focus area quiz id: {}", adaptiveLearningQuizWebVO.getID());

        // Find the actual CourseAssessmentFocusArea that was newly built and added to the curriculum as an assessment
        Optional<CourseAssessmentFocusArea> optionalCourseAssessmentFocusArea = curriculumProficiencyAssessment.getCourseAssessmentFocusAreaByID(courseAssessmentFocusAreaVO.getFocusAreaUniqueID());


        if (optionalCourseAssessmentFocusArea.isPresent()) {
            LOGGER.info("Modifying CourseAssessmentFocusArea to reflect updated quiz : {}", optionalCourseAssessmentFocusArea.get());
            iCourseAssessmentFocusAreaService.modifyCourseAssessmentFocusAreaWithQuiz(optionalCourseAssessmentFocusArea.get(), adaptiveLearningQuizWebVO.getID());
        }

        model.addAttribute("AssessmentFocusAreas", curriculumProficiencyAssessment.getCourseAssessmentFocusAreas());

        return ContentRootE.Content_Admin_Curriculum_Assessment.getContentRootPagePath("assessment-admin-launch");
    }




























    @RequestMapping(value = "/process-assessment-details", method = RequestMethod.POST)
    public String saveAdaptiveQuizDetails(Model model,
                                          @ModelAttribute(CourseAssessmentFocusAreaVO.CLASS_ATTRIBUTE_IDENTIFIER) CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO) {
        LOGGER.info("Attempting to save Assessment details for courseAssessmentFocusAreaVO: {}", courseAssessmentFocusAreaVO);

        // Get the list of all questions and answers as a map to return for display.
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = courseAssessmentFocusAreaVO.getIAdaptiveLearningQuizQuestionVOs();
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);
        return ContentRootE.Content_Admin_Curriculum_Assessment.getContentRootPagePath("customize-assessment");
    }

    @RequestMapping(value = "/add-assessment-quiz-question-type", method = RequestMethod.GET)
    public String addQuizQuestionType(Model model,
                                      @RequestParam("quizQuestionType") String quizQuestionType,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Adding Assessment Quiz question type: {}", quizQuestionType);

        // IF this is a result of a redirect add any web operations errrors to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        // Look up quiz question type
        AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE = AdaptiveLearningQuizQuestionTypeE.getByName(quizQuestionType);

        LOGGER.info("Constructed AdaptiveLearningQuizQuestionVO object...");
        AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO = new AdaptiveLearningQuizQuestionVO();
        adaptiveLearningQuizQuestionVO.setAdaptiveLearningQuizQuestionTypeE(adaptiveLearningQuizQuestionTypeE);

        model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), adaptiveLearningQuizQuestionVO);

        // Add URL for the generic quiz question pages to submit adding of questions and answers back to this controller
        model.addAttribute(AdaptiveLearningQuizAttributeE.QuizQuestionTypeSubmissionURL.toString(), "");

        return ContentRootE.Content_Admin_Curriculum_Assessment.getContentRootPagePath("assessment-question-by-type");
    }

    @RequestMapping(value = "/save-assessment-quiz-question", method = RequestMethod.POST)
    public String addQuizQuestionDetails(Model model,
                                         @ModelAttribute(ELearningCurriculum.CLASS_ATTRIBUTE_IDENTIFIER) ELearningCurriculum eLearningCurriculum,
                                         @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
                                         @ModelAttribute("AdaptiveLearningQuizQuestionVO") AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO) {
        LOGGER.info("Adding Quiz question details to model for adaptiveLearningQuizQuestionVO: {}", adaptiveLearningQuizQuestionVO);


        // Build and add all question and provided answers to Quiz
        adaptiveLearningQuizQuestionVO.buildAndAddQuestionAnswerModel();

        // Validate the question details, IF anything is missing redirect back to Quiz Question page
        Optional<String> optionalValidationMessage = adaptiveLearningQuizQuestionVO.getValidationMessage();
        if(optionalValidationMessage.isPresent()) {
            LOGGER.warn("Found Validation Issue: {}, redirecting back to Question entry...", optionalValidationMessage.get());
            model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), adaptiveLearningQuizQuestionVO);
            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), optionalValidationMessage.get());
            //iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
            return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz-question-type");
        }

        if (adaptiveLearningQuizQuestionVO.getID() == null) {
            LOGGER.info("Saving new QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
            int questionOrder = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs().size() + 1;
            adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(eLearningCurriculum);
            adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
            //iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, questionOrder);
        } else {
            LOGGER.info("Updating previously saved QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
            //iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, 0);
            adaptiveLearningQuizWebVO.replaceAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
        }

        // Get the list of all questions and answers as a map to return for display.
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);

        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
    }


}
