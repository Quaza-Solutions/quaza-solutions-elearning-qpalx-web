package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.lms.assessment.CourseAssessmentFocusArea;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.subjectmatter.proficiency.ProficiencyRankingScaleE;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialLevel;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.TutorialLevelCalendar;
import com.quaza.solutions.qpalx.elearning.service.institutions.IQPalXEducationalInstitutionService;
import com.quaza.solutions.qpalx.elearning.service.lms.assessment.CourseAssessmentFocusAreaService;
import com.quaza.solutions.qpalx.elearning.service.lms.assessment.ICourseAssessmentFocusAreaService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXELessonService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.ITutorialLevelCalendarService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.curriculum.ICurriculumHierarchyService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.AcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IContentAdminTutorialGradePanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.*;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXELessonWebVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author manyce400
 */
@Controller
public class QPalXLessonAdminController {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXELessonService")
    private IQPalXELessonService iqPalXELessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultELearningCourseService")
    private IELearningCourseService ieLearningCourseService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalXEducationalInstitutionService")
    private IQPalXEducationalInstitutionService iqPalXEducationalInstitutionService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService")
    private IELearningStaticContentService ieLearningStaticContentService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.web.ContentAdminTutorialGradePanelService")
    private IContentAdminTutorialGradePanelService contentAdminTutorialGradePanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.CurriculumHierarchyService")
    private ICurriculumHierarchyService iCurriculumHierarchyService;

    @Autowired
    @Qualifier(CourseAssessmentFocusAreaService.BEAN_NAME)
    private ICourseAssessmentFocusAreaService iCourseAssessmentFocusAreaService;

    @Autowired
    @Qualifier(AcademicLevelAdminPanelService.BEAN_NAME)
    private IAcademicLevelAdminPanelService iAcademicLevelAdminPanelService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultTutorialLevelCalendarService")
    private ITutorialLevelCalendarService iTutorialLevelCalendarService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QPalXLessonAdminController.class);



    @RequestMapping(value = "/view-admin-qpalx-elessons", method = RequestMethod.GET)
    public String viewAdminQPalXLessons(final Model model, @RequestParam("eLearningCourseID") String eLearningCourseID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Loading and displaying view for all QPalXELesson for eLearningCourseID: {}", eLearningCourseID);

        // Add any redirect attributes to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        // Add information required for Users account info display panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCourse.toString(), Boolean.TRUE.toString());

        // Add all attributes required for content admin tutorial panel
        Long courseID = NumberUtils.toLong(eLearningCourseID);
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(courseID);
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);
        contentAdminTutorialGradePanelService.addDisplayPanelAttributes(model, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, eLearningCourse);

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        // Find all the QPalXELesson's currently available
        List<QPalXELesson> qPalXELessons = iqPalXELessonService.findQPalXELessonByCourse(eLearningCourse);
        model.addAttribute(LessonsAdminAttributesE.QPalXELessons.toString(), qPalXELessons);

        // Find any created Assessment for this course
        List<CourseAssessmentFocusArea> courseAssessmentFocusAreas = iCourseAssessmentFocusAreaService.findCourseAssessmentFocusAreas(eLearningCourse);
        model.addAttribute(CourseAssessmentFocusArea.CLASS_ATTRIBUTE_IDENTIFIER, courseAssessmentFocusAreas);

        return ContentRootE.Content_Admin_Lessons.getContentRootPagePath("view-qpalx-elessons");
    }

    @RequestMapping(value = "/add-qpalx-elesson", method = RequestMethod.GET)
    public String addQPalXELessonsView(final Model model, @RequestParam("eLearningCourseID") String eLearningCourseID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Building add QPalxELesson page options for eLearningCourseID: {}", eLearningCourseID);

        // IF this is a result of a redirect add any web operations errrors to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Create value object used to bind form elements
        QPalXELessonWebVO qPalXELessonWebVO = new QPalXELessonWebVO();

        // Add all required attributes to dispaly add qpalx elesson page
        Long courseID = NumberUtils.toLong(eLearningCourseID);
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(courseID);
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        StudentTutorialLevel studentTutorialLevel = eLearningCurriculum.getStudentTutorialGrade().getStudentTutorialLevel();
        List<TutorialLevelCalendar> tutorialLevelCalendars = iTutorialLevelCalendarService.findAllByStudentTutorialLevel(studentTutorialLevel);
        model.addAttribute(TutorialLevelCalendar.CLASS_ATTRIBUTE_INSTANCES, tutorialLevelCalendars);

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        List<QPalXEducationalInstitution> qPalXEducationalInstitutions = iqPalXEducationalInstitutionService.findAll();
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);
        model.addAttribute(DomainDataDisplayAttributeE.AvailableQPalXEducationalInstitutions.toString(), qPalXEducationalInstitutions);
        model.addAttribute(DomainDataDisplayAttributeE.ProficiencyRankings.toString(), ProficiencyRankingScaleE.lowestToHighest());
        model.addAttribute(ValueObjectDataDisplayAttributeE.QPalXELessonWebVO.toString(), qPalXELessonWebVO);
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXELessonWebVO.getQPalXTutorialContentTypes());

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        return ContentRootE.Content_Admin_Lessons.getContentRootPagePath("add-qpalx-elesson");
    }

    @RequestMapping(value = "/edit-qpalx-elesson", method = RequestMethod.GET)
    public String editQPalXELessonsView(final Model model, @RequestParam("qpalxELessonID") String qpalxELessonID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Generating edit Lesson page for qpalxELessonID: {}", qpalxELessonID);

        Long id = NumberUtils.toLong(qpalxELessonID);
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(id);
        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        StudentTutorialLevel studentTutorialLevel = eLearningCurriculum.getStudentTutorialGrade().getStudentTutorialLevel();
        List<TutorialLevelCalendar> tutorialLevelCalendars = iTutorialLevelCalendarService.findAllByStudentTutorialLevel(studentTutorialLevel);
        model.addAttribute(TutorialLevelCalendar.CLASS_ATTRIBUTE_INSTANCES, tutorialLevelCalendars);

        // Create value object used to bind form elements
        QPalXELessonWebVO qPalXELessonWebVO = new QPalXELessonWebVO(qPalXELesson);
        model.addAttribute(ValueObjectDataDisplayAttributeE.QPalXELessonWebVO.toString(), qPalXELessonWebVO);

        // Add all attributes required for page
        List<QPalXEducationalInstitution> qPalXEducationalInstitutions = iqPalXEducationalInstitutionService.findAll();
        model.addAttribute(DomainDataDisplayAttributeE.AvailableQPalXEducationalInstitutions.toString(), qPalXEducationalInstitutions);
        model.addAttribute(DomainDataDisplayAttributeE.ProficiencyRankings.toString(), ProficiencyRankingScaleE.lowestToHighest());
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXELessonWebVO.getQPalXTutorialContentTypes());
        return ContentRootE.Content_Admin_Lessons.getContentRootPagePath("edit-qpalx-elesson");
    }

    @RequestMapping(value = "/save-qpalx-elesson", method = RequestMethod.POST)
    public void saveQPalXELesson(Model model,
                                 @ModelAttribute("QPalXELessonWebVO") QPalXELessonWebVO qPalXELessonWebVO,
                                 HttpServletRequest request, HttpServletResponse response, @RequestParam("narration_file") MultipartFile multipartFile) {
        LOGGER.info("Saving QPalX ELesson with narration_file and VO attributes: {}", qPalXELessonWebVO);

        System.out.println("multipartFile = " + multipartFile);

        // Build hierarchy based content structure on the Lesson which will allow for uploading content to the right directory structure
        iCurriculumHierarchyService.buildHierarchyForQPalXELessonWebVO(qPalXELessonWebVO);

        // Upload file and create the ELearningMediaContent
        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXELessonWebVO);

        if(eLearningMediaContent == null) {
            LOGGER.warn("Selected ELearning Media content could not be uploaded.  Check selected file content.");
            String targetURL = "/add-qpalx-elesson?eLearningCourseID=" + qPalXELessonWebVO.getELearningCourseID();
            String errorMessage = "Failed to upload file: Check the contents of the file";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else if(eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Uploaded course activity media content file is currently not supported...");
            String targetURL = "/add-qpalx-elesson?eLearningCourseID=" + qPalXELessonWebVO.getELearningCourseID();
            String errorMessage = "Uploaded file is not supported: Only Files of type(MP4, SWF) supported";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else {
            LOGGER.info("QPalX Lesson media content was succesfully uploaded, saving lesson details...");
            qPalXELessonWebVO.setELearningMediaContent(eLearningMediaContent);
            iqPalXELessonService.createAndSaveQPalXELesson(qPalXELessonWebVO);
            String targetURL = "/view-admin-qpalx-elessons?eLearningCourseID=" + qPalXELessonWebVO.getELearningCourseID();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        }
    }

//    @RequestMapping(value = "/save-qpalx-elesson", method = RequestMethod.POST)
//    public void saveQPalXELessonNoFile(Model model,
//                                       @ModelAttribute("QPalXELessonWebVO") QPalXELessonWebVO qPalXELessonWebVO,
//                                       HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Saving QPalX ELesson with VO attributes: {}", qPalXELessonWebVO);
//
//
//        // Build hierarchy based content structure on the Lesson which will allow for uploading content to the right directory structure
//        iCurriculumHierarchyService.buildHierarchyForQPalXELessonWebVO(qPalXELessonWebVO);
//
//        LOGGER.info("QPalX Lesson media content was succesfully uploaded, saving lesson details...");
//        iqPalXELessonService.createAndSaveQPalXELesson(qPalXELessonWebVO);
//        String targetURL = "/view-admin-qpalx-elessons?eLearningCourseID=" + qPalXELessonWebVO.getELearningCourseID();
//        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
//    }

    @RequestMapping(value = "/update-qpalx-elesson", method = RequestMethod.POST)
    public void updateQPalXELesson(Model model, @ModelAttribute("QPalXELessonWebVO") QPalXELessonWebVO qPalXELessonWebVO,
                                   HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile multipartFile) {
        LOGGER.info("Saving QPalX ELesson with VO attributes: {}", qPalXELessonWebVO);

        // Build hierarchy based content structure on the Lesson which will allow for uploading content to the right directory structure
        iCurriculumHierarchyService.buildHierarchyForQPalXELessonWebVO(qPalXELessonWebVO);

        // Upload the new file and create the ELearningMediaContent
        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXELessonWebVO);

        if(eLearningMediaContent == null) {
            LOGGER.warn("Selected ELearning Media content could not be uploaded.  Check selected file content.");
            String targetURL = "/edit-qpalx-elesson?eLearningCourseID=" + qPalXELessonWebVO.getELearningCourseID();
            String errorMessage = "Failed to upload file: Check the contents of the file";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else if(eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Uploaded course activity media content file is currently not supported...");
            String targetURL = "/edit-qpalx-elesson?eLearningCourseID=" + qPalXELessonWebVO.getELearningCourseID();
            String errorMessage = "Uploaded file is not supported: Only Files of type(MP4, SWF) supported";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else {
            LOGGER.info("QPalX Lesson media content was succesfully uploaded, saving lesson details...");
            qPalXELessonWebVO.setELearningMediaContent(eLearningMediaContent);

            // Delete the current existing Lesson Intro video
            QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(qPalXELessonWebVO.getQPalxELessonID());
            LOGGER.debug("Deleting current existing intro video media contet: {}", qPalXELesson.geteLearningMediaContent());
            ieLearningStaticContentService.deleteELearningMediaContent(qPalXELesson.geteLearningMediaContent());

            iqPalXELessonService.updateAndSaveQPalXELesson(qPalXELesson, qPalXELessonWebVO);
            String targetURL = "/view-admin-qpalx-elessons?eLearningCourseID=" + qPalXELessonWebVO.getELearningCourseID();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        }
    }


    @RequestMapping(value = "/delete-qpalx-elesson", method = RequestMethod.GET)
    public void deleteQPalXELessons(final Model model, @RequestParam("qpalxELessonID") String qpalxELessonID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Attempting to delete QPalX Lesson with ID: {}", qpalxELessonID);

        // Load up the lesson and then delete
        Long lessonID = NumberUtils.toLong(qpalxELessonID);
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(lessonID);
        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();

        // check to see if this lesson can be deleted first
        boolean isDeletable = iqPalXELessonService.isELessonDeletable(qPalXELesson);
        if (!isDeletable) {
            String targetURL = "/view-admin-qpalx-elessons?eLearningCourseID=" + eLearningCourse.getId();
            String error = new StringBuilder("Lesson:  ")
                    .append(qPalXELesson.getLessonName()).append("  => ")
                    .append(" Cannot be deleted.  Remove all MicroLessons, QuestionBanks and Quizzes first")
                    .toString();
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, error, WebOperationErrorAttributesE.Invalid_Delete_Operation, request, response);
        } else {
            iqPalXELessonService.deleteQPalXELesson(qPalXELesson);
            ieLearningStaticContentService.deleteELearningMediaContent(qPalXELesson.geteLearningMediaContent());
            String targetURL = "/view-admin-qpalx-elessons?eLearningCourseID=" + eLearningCourse.getId();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        }
    }
}
