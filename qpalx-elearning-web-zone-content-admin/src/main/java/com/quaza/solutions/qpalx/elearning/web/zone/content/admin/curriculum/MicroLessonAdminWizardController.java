package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXELessonService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.curriculum.ICurriculumHierarchyService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.AcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ValueObjectDataDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.WebOperationErrorAttributesE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXEMicroLessonVO;
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

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(
        value = {QPalXEMicroLessonVO.CLASS_ATTRIBUTE, "SelectedQPalXELesson"}
)
public class MicroLessonAdminWizardController {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXELessonService")
    private IQPalXELessonService iqPalXELessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXEMicroLessonService")
    private IQPalXEMicroLessonService iqPalXEMicroLessonService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.CurriculumHierarchyService")
    private ICurriculumHierarchyService iCurriculumHierarchyService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService")
    private IELearningStaticContentService ieLearningStaticContentService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier(AcademicLevelAdminPanelService.BEAN_NAME)
    private IAcademicLevelAdminPanelService iAcademicLevelAdminPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MicroLessonAdminWizardController.class);


    @RequestMapping(value = "/microlesson-create-start", method = RequestMethod.GET)
    public String microLessonStartWizard(ModelMap modelMap, Model model, @RequestParam("qpalxELessonID") Long qpalxELessonID) {
        LOGGER.info("Starting create MicroLesson Wizard for Lesson with ID: {}", qpalxELessonID);

        QPalXEMicroLessonVO qPalXEMicroLessonVO = new QPalXEMicroLessonVO();
        modelMap.addAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE, qPalXEMicroLessonVO);

        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Add all required attributes to dispaly add qpalx elesson page
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(qpalxELessonID);
        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        modelMap.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getQPalXTutorialContentTypes());
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedStaticQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getStaticQPalXTutorialContentTypes());

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("create-micro-lesson-info");
    }

    @RequestMapping(value = "/microlesson-narration-select", method = RequestMethod.POST)
    public String microLessonNarrationSelect(ModelMap modelMap, Model model,
                                             @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                             @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO) {
        LOGGER.info("Building view for MicroLesson narration file selection page: {}", qPalXEMicroLessonVO);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getQPalXTutorialContentTypes());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-narration");
    }

    @RequestMapping(value = "/skip-microlesson-narration", method = RequestMethod.POST)
    public String saveMicroLessonNarrationFile(Model model,
                                               @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO) {
        LOGGER.info("Skipping narration file creation for qPalXEMicroLessonVO: {}", qPalXEMicroLessonVO);
        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-static");
    }

    @RequestMapping(value = "/save-microlesson-narration", method = RequestMethod.POST)
    public String saveMicroLessonNarrationFile(Model model,
                                               @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                               @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO,
                                               @RequestParam("narration_file") MultipartFile multipartFile) {
        LOGGER.info("Saving QPalX Micro lesson narration file with VO attributes: {}", qPalXEMicroLessonVO);
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        iCurriculumHierarchyService.buildHierarchyForQPalXEMicroLessonVO(qPalXEMicroLessonVO);
        qPalXEMicroLessonVO.setQPalXTutorialContentType(QPalXTutorialContentTypeE.Video.toString());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        // Upload file and create the ELearningMediaContent
        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXEMicroLessonVO);

        if(eLearningMediaContent == null || eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Failed to upload MicroLesson narration file...");
            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), "Error occurred on fileupload, check the file type and content");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-narration");
        } else {
            LOGGER.info("Upload of narration file was succeful, setting ELearningMediaContent for narration");
            qPalXEMicroLessonVO.setELearningMediaContent(eLearningMediaContent);
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-static");
        }
    }

    @RequestMapping(value = "/save-microlesson-static", method = RequestMethod.POST)
    public String saveMicroLessonStaticFile(Model model,
                                            @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                            @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO,
                                            @RequestParam("static_file") MultipartFile multipartFile) {
        LOGGER.info("Saving QPalX Micro lesson static file with VO attributes: {}", qPalXEMicroLessonVO);
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        iCurriculumHierarchyService.buildHierarchyForQPalXEMicroLessonVO(qPalXEMicroLessonVO);
        qPalXEMicroLessonVO.setQPalXTutorialContentType(QPalXTutorialContentTypeE.Static.toString());

        // Upload file and create the ELearningMediaContent
        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXEMicroLessonVO);

        if(eLearningMediaContent == null || eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Failed to upload MicroLesson narration file...");
            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), "Error occurred on fileupload, check the file type and content");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-static");
        } else {
            LOGGER.info("Upload of narration file was succeful, setting ELearningMediaContent for narration");
            qPalXEMicroLessonVO.setStaticELearningMediaContent(eLearningMediaContent);
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-interactive");
        }
    }

    @RequestMapping(value = "/skip-microlesson-static", method = RequestMethod.POST)
    public String saveMicroLessonStaticFile(Model model,
                                            @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO) {
        LOGGER.info("Skipping narration file creation for qPalXEMicroLessonVO: {}", qPalXEMicroLessonVO);
        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-interactive");
    }

    @RequestMapping(value = "/save-microlesson-interactive", method = RequestMethod.POST)
    public String saveMicroLessonInteractiveFile(Model model, SessionStatus status,
                                                 @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                                 @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO,
                                                 @RequestParam("interactive_file") MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Saving QPalX Micro lesson narration file with VO attributes: {}", qPalXEMicroLessonVO);
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        iCurriculumHierarchyService.buildHierarchyForQPalXEMicroLessonVO(qPalXEMicroLessonVO);
        qPalXEMicroLessonVO.setQPalXTutorialContentType(QPalXTutorialContentTypeE.Interactive_Excersise.toString());

        // Upload file and create the ELearningMediaContent
        ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXEMicroLessonVO);

        if(eLearningMediaContent == null || eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Failed to upload MicroLesson narration file...");
            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), "Error occurred on fileupload, check the file type and content");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-interactive");
        } else {
            LOGGER.info("Upload of narration file was succeful, setting ELearningMediaContent for narration, saving final MicroLesson details...");
            qPalXEMicroLessonVO.setInteractiveELearningMediaContent(eLearningMediaContent);
            iqPalXEMicroLessonService.createAndSaveQPalXEMicroLesson(qPalXEMicroLessonVO);

            status.setComplete();

            // redirect back to view all micro lessons in this Lesson.
            String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXEMicroLessonVO.getQPalXELessonID();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);

            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-static");
        }
    }

    @RequestMapping(value = "/complete-microlesson-wizard", method = RequestMethod.POST)
    public void completeMicroLessonCreateWizard(Model model, SessionStatus status,
                                               @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO,
                                               HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Completing MicroLesson creation wizard....");
        status.setComplete();
        iqPalXEMicroLessonService.createAndSaveQPalXEMicroLesson(qPalXEMicroLessonVO);
        String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXEMicroLessonVO.getQPalXELessonID();
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

    @RequestMapping(value = "/exit-microlesson-create", method = RequestMethod.GET)
    public void exitMicroLessonCreateWizard(Model model, SessionStatus status,
                                            @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                            HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Canceling creation of MicroLesson wizard for Lesson: {}", qPalXELesson.getId());
        String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXELesson.getId();
        status.setComplete();
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

}
