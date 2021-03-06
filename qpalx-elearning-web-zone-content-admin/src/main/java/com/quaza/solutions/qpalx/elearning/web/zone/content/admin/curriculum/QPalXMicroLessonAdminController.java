package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.scorable.QuestionBankItem;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.*;
import com.quaza.solutions.qpalx.elearning.service.institutions.IQPalXEducationalInstitutionService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.scorable.IQuestionBankService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXELessonService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.curriculum.ICurriculumHierarchyService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.AcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IContentAdminTutorialGradePanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.*;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXEMicroLessonVO;
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
public class QPalXMicroLessonAdminController {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXELessonService")
    private IQPalXELessonService iqPalXELessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXEMicroLessonService")
    private IQPalXEMicroLessonService iqPalXEMicroLessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalXEducationalInstitutionService")
    private IQPalXEducationalInstitutionService iqPalXEducationalInstitutionService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QuestionBankService")
    private IQuestionBankService iQuestionBankService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.web.ContentAdminTutorialGradePanelService")
    private IContentAdminTutorialGradePanelService contentAdminTutorialGradePanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService")
    private IELearningStaticContentService ieLearningStaticContentService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.CurriculumHierarchyService")
    private ICurriculumHierarchyService iCurriculumHierarchyService;

    @Autowired
    @Qualifier(AcademicLevelAdminPanelService.BEAN_NAME)
    private IAcademicLevelAdminPanelService iAcademicLevelAdminPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QPalXLessonAdminController.class);



    @RequestMapping(value = "/move-micro-lesson-down", method = RequestMethod.GET)
    public void moveAdaptiveLearningQuizDown(@RequestParam("microlessonID") Long microlessonID,
                                             HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Moving MicroLesson with ID: {} down", microlessonID);
        QPalXEMicroLesson microLesson = iqPalXEMicroLessonService.findByID(microlessonID);
        QPalXELesson qPalXELesson = microLesson.getQPalXELesson();
        iqPalXEMicroLessonService.moveDown(microLesson);
        String url = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXELesson.getId();
        iRedirectStrategyExecutor.sendRedirect(request, response, url);
    }

    @RequestMapping(value = "/move-micro-lesson-up", method = RequestMethod.GET)
    public void moveAdaptiveLearningQuizUp(@RequestParam("microlessonID") Long microlessonID,
                                           HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Moving MicroLesson with ID: {} up", microlessonID);
        QPalXEMicroLesson microLesson = iqPalXEMicroLessonService.findByID(microlessonID);
        QPalXELesson qPalXELesson = microLesson.getQPalXELesson();
        iqPalXEMicroLessonService.moveUp(microLesson);
        String url = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXELesson.getId();
        iRedirectStrategyExecutor.sendRedirect(request, response, url);
    }


    @RequestMapping(value = "/view-admin-qpalx-micro-elessons", method = RequestMethod.GET)
    public String viewAdminQPalXLessons(final Model model, @RequestParam("qpalxELessonID") String qpalxELessonID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Loading and displaying view for all QPalXMicroLessons for qpalxELessonID: {}", qpalxELessonID);

        // Add any redirect attributes to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        // Add information required for Users account info display panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayLesson.toString(), Boolean.TRUE.toString());

        // Add all attributes required for content admin tutorial panel
        Long lessonID = NumberUtils.toLong(qpalxELessonID);
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(lessonID);
        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);
        contentAdminTutorialGradePanelService.addDisplayPanelAttributes(model, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, qPalXELesson);

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        // Find all the QPalXELesson's currently available
        List<QPalXEMicroLesson> qPalXEMicroLessons = iqPalXEMicroLessonService.findQPalXEMicroLessons(qPalXELesson);
        model.addAttribute(LessonsAdminAttributesE.QPalXEMicroLessons.toString(), qPalXEMicroLessons);

        // Add all Question banks for this Lesson
        List<QuestionBankItem> questionBankItems = iQuestionBankService.findQuestionBankItems(qPalXELesson);
        model.addAttribute(LessonsAdminAttributesE.QuestionBankItems.toString(), questionBankItems);
        return ContentRootE.Content_Admin_Lessons.getContentRootPagePath("view-qpalx-microlessons");
    }

    @RequestMapping(value = "/add-qpalx-microlesson", method = RequestMethod.GET)
    public String addMicroLessonsView(final Model model, @RequestParam("qpalxELessonID") String qpalxELessonID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Building add QPalxELesson page options for qpalxELessonID: {}", qpalxELessonID);

        // IF this is a result of a redirect add any web operations errrors to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        // Create value object used to bind form elements
        QPalXEMicroLessonVO qPalXEMicroLessonVO = new QPalXEMicroLessonVO();
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Add all required attributes to dispaly add qpalx elesson page
        Long lessonID = NumberUtils.toLong(qpalxELessonID);
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(lessonID);
        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);
        model.addAttribute(ValueObjectDataDisplayAttributeE.QPalXEMicroLessonVO.toString(), qPalXEMicroLessonVO);
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getQPalXTutorialContentTypes());
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedStaticQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getStaticQPalXTutorialContentTypes());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        return ContentRootE.Content_Admin_Lessons.getContentRootPagePath("add-qpalx-microlesson");
    }

    @RequestMapping(value = "/edit-qpalx-micro-lesson", method = RequestMethod.GET)
    public String editAdminQPalxMicroLessons(final Model model, @RequestParam("microLessonID") String microLessonID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Loading and displaying Edit view for microLessonID: {}", microLessonID);

        // IF this is a result of a redirect add any web operations errrors to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        // Add all attributes required for content admin tutorial panel
        Long id = NumberUtils.toLong(microLessonID);
        QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(id);
        QPalXEMicroLessonVO qPalXEMicroLessonVO = new QPalXEMicroLessonVO(qPalXEMicroLesson);

        model.addAttribute(ValueObjectDataDisplayAttributeE.QPalXEMicroLessonVO.toString(), qPalXEMicroLessonVO);
        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXMicroLesson.toString(), qPalXEMicroLesson);
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getQPalXTutorialContentTypes());
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedStaticQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getStaticQPalXTutorialContentTypes());

        return ContentRootE.Content_Admin_Lessons.getContentRootPagePath("modify-qpalx-microlesson");
    }


    @RequestMapping(value = "/update-qpalx-microlesson", method = RequestMethod.POST)
    public void updateQPalXMicroLesson(Model model, @ModelAttribute("QPalXEMicroLessonVO") QPalXEMicroLessonVO qPalXEMicroLessonVO,
                                       HttpServletRequest request, HttpServletResponse response, @RequestParam("narration_file") MultipartFile multipartFile, @RequestParam("static_file") MultipartFile staticMultipartFile) {
        LOGGER.info("Updating QPalX micro lesson with VO attributes: {} ...", qPalXEMicroLessonVO);

        // Build hierarchy based content structure on the Lesson which will allow for uploading content to the right directory structure
        iCurriculumHierarchyService.buildHierarchyForQPalXEMicroLessonVO(qPalXEMicroLessonVO);

        // Upload file and create the ELearningMediaContent
        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXEMicroLessonVO);
        ELearningMediaContent staticELearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(staticMultipartFile, qPalXEMicroLessonVO);

        if(eLearningMediaContent == null || staticELearningMediaContent == null) {
            LOGGER.warn("Selected ELearning Media content could not be uploaded.  Check selected file content.");
            String targetURL = "/edit-qpalx-micro-lesson?qpalxELessonID=" + qPalXEMicroLessonVO.getQPalXELessonID();
            String errorMessage = "Failed to upload file: Check the contents of the file";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else if(eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT || staticELearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Uploaded course activity media content file is currently not supported...");
            String targetURL = "/edit-qpalx-micro-lesson?qpalxELessonID=" + qPalXEMicroLessonVO.getQPalXELessonID();
            String errorMessage = "Uploaded file is not supported: Only Files of type(MP4, SWF) supported";
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
        } else {
            LOGGER.info("QPalX MicroLesson media content was succesfully uploaded, deleting old files and updating micro lesson details...");
            QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(qPalXEMicroLessonVO.getQPalXELessonID());

            // delete both the narration and static files
            ieLearningStaticContentService.deleteELearningMediaContent(qPalXEMicroLesson.geteLearningMediaContent());
            ieLearningStaticContentService.deleteELearningMediaContent(qPalXEMicroLesson.getStaticELearningMediaContent());

            qPalXEMicroLessonVO.setELearningMediaContent(eLearningMediaContent);
            qPalXEMicroLessonVO.setStaticELearningMediaContent(staticELearningMediaContent);
            iqPalXEMicroLessonService.updateAndSaveQPalXEMicroLesson(qPalXEMicroLesson, qPalXEMicroLessonVO);
            String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXEMicroLessonVO.getQPalXELessonID();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        }

    }


    @RequestMapping(value = "/delete-qpalx-micro-lesson", method = RequestMethod.GET)
    public void deleteQPalXMicroLessons(final Model model, @RequestParam("microLessonID") String microLessonID, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Attempting to delete QPalX Micro Lesson with ID: {}", microLessonID);

        // Load up the lesson and then delete
        Long id = NumberUtils.toLong(microLessonID);
        QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(id);
        QPalXELesson qPalXELesson = qPalXEMicroLesson.getQPalXELesson();

        // check to see if this lesson can be deleted first
        boolean isDeletable = iqPalXEMicroLessonService.isMicroLessonDeletable(qPalXEMicroLesson);
        if (!isDeletable) {
            String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXELesson.getId();
            String error = new StringBuilder("MicroLesson:  ")
                    .append(qPalXEMicroLesson.getMicroLessonName()).append("  => ")
                    .append(" Cannot be deleted.  Remove all Quizzes first")
                    .toString();
            iRedirectStrategyExecutor.sendRedirectWithError(targetURL, error, WebOperationErrorAttributesE.Invalid_Delete_Operation, request, response);
        } else {
            iqPalXEMicroLessonService.delete(qPalXEMicroLesson);
            if (qPalXEMicroLesson.geteLearningMediaContent() != null) {
                ieLearningStaticContentService.deleteELearningMediaContent(qPalXEMicroLesson.geteLearningMediaContent());
            }
            String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXELesson.getId();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        }
    }

}
