package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.*;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXELessonService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.curriculum.ICurriculumHierarchyService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.AcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.*;
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
        value = {QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER, QPalXEMicroLessonVO.CLASS_ATTRIBUTE, "MicroLessonSavedAndUpdated", "SelectedQPalXELesson"}
)
public class MicroLessonEditAdminWizardController {



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


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MicroLessonEditAdminWizardController.class);


    @RequestMapping(value = "/microlesson-edit-start", method = RequestMethod.GET)
    public String microLessonStartWizard(ModelMap modelMap, Model model,@RequestParam("microLessonID") Long microLessonID) {
        LOGGER.info("Starting edit MicroLesson Wizard for MicroLesson with ID: {}", microLessonID);


        // Add all required attributes to dispaly add qpalx elesson page
        QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(microLessonID);
        QPalXELesson qPalXELesson = qPalXEMicroLesson.getQPalXELesson();
        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        QPalXEMicroLessonVO qPalXEMicroLessonVO = new QPalXEMicroLessonVO(qPalXEMicroLesson);

        modelMap.addAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER, qPalXEMicroLesson);
        modelMap.addAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE, qPalXEMicroLessonVO);
        modelMap.addAttribute("MicroLessonSavedAndUpdated", Boolean.FALSE);
        modelMap.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getQPalXTutorialContentTypes());
        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedStaticQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getStaticQPalXTutorialContentTypes());

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("edit-micro-lesson-info");
    }

    @RequestMapping(value = "/microlesson-edit-narration", method = RequestMethod.POST)
    public String microLessonNarrationSelect(ModelMap modelMap, Model model,
                                             @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                             @ModelAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER) QPalXEMicroLesson qPalXEMicroLesson,
                                             @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO) {
        LOGGER.info("Building view for MicroLesson narration file selection page: {}", qPalXEMicroLessonVO);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        model.addAttribute(ValueObjectDataDisplayAttributeE.SupportedQPalXTutorialContentTypes.toString(), qPalXEMicroLessonVO.getQPalXTutorialContentTypes());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Save the changes just made in case user doesn't complete edit session
        qPalXEMicroLessonVO.setELearningMediaContent(qPalXEMicroLesson.geteLearningMediaContent());
        qPalXEMicroLessonVO.setStaticELearningMediaContent(qPalXEMicroLesson.getStaticELearningMediaContent());
        qPalXEMicroLessonVO.setInteractiveELearningMediaContent(qPalXEMicroLesson.getInteractiveELearningMediaContent());
        iqPalXEMicroLessonService.updateAndSaveQPalXEMicroLesson(qPalXEMicroLesson, qPalXEMicroLessonVO);
        modelMap.addAttribute("MicroLessonSavedAndUpdated", Boolean.TRUE);

        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("edit-micro-lesson-narration");
    }

    @RequestMapping(value = "/skip-edit-microlesson-narration", method = RequestMethod.POST)
    public String skipEditMicroLessonNarrationFile(Model model,
                                               @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO) {
        LOGGER.info("Skipping narration file creation for qPalXEMicroLessonVO: {}", qPalXEMicroLessonVO);
        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("edit-micro-lesson-static");
    }

    @RequestMapping(value = "/save-edited-microlesson-narration", method = RequestMethod.POST)
    public String saveMicroLessonNarrationFile(Model model, ModelMap modelMap,
                                               @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                               @ModelAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER) QPalXEMicroLesson qPalXEMicroLesson,
                                               @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO,
                                               @RequestParam("narration_file") MultipartFile multipartFile) {
        LOGGER.info("Edit-Mode:  Saving newly uploaded QPalX-Micro-Lesson narration file...");
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();

        iCurriculumHierarchyService.buildHierarchyForQPalXEMicroLessonVO(qPalXEMicroLessonVO);
        qPalXEMicroLessonVO.setQPalXTutorialContentType(QPalXTutorialContentTypeE.Video.toString());

        // Add Admin AcademicLevel Panel data
        iAcademicLevelAdminPanelService.addAdministratorAcademicGradeLevels(model, eLearningCurriculum.getCurriculumType(), eLearningCurriculum.getStudentTutorialGrade());

        // Upload newly created narration file from edited mode
        ELearningMediaContent narrationELearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXEMicroLessonVO);

        if(narrationELearningMediaContent == null || narrationELearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Failed to upload MicroLesson narration file...");
            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), "Error occurred on fileupload, check the file type and content");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("edit-micro-lesson-narration");
        } else {
            // Delete the previously uploaded narration file IF exists
            if (qPalXEMicroLesson.geteLearningMediaContent() != null) {
                LOGGER.info("Deleting previously saved MicroLesson Narration file: {}", qPalXEMicroLesson.geteLearningMediaContent());
                ieLearningStaticContentService.deleteELearningMediaContent(qPalXEMicroLesson.geteLearningMediaContent());
            }

            // Update new narration file on micro lesson
            qPalXEMicroLesson.seteLearningMediaContent(narrationELearningMediaContent);
            qPalXEMicroLessonVO.setELearningMediaContent(narrationELearningMediaContent);

            // Save updated QPalXMicroLesson here with new narration file in case user abandons edit
            iqPalXEMicroLessonService.updateAndSaveQPalXEMicroLesson(qPalXEMicroLesson, qPalXEMicroLessonVO);
            modelMap.addAttribute("MicroLessonSavedAndUpdated", Boolean.TRUE);

            LOGGER.info("Upload of narration file was succeful, setting ELearningMediaContent for narration");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("edit-micro-lesson-static");
        }
    }


    @RequestMapping(value = "/skip-edit-microlesson-static", method = RequestMethod.POST)
    public String saveMicroLessonStaticFile(Model model,
                                            @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO) {
        LOGGER.info("Skipping narration file creation for qPalXEMicroLessonVO: {}", qPalXEMicroLessonVO);
        return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("edit-micro-lesson-interactive");
    }

    @RequestMapping(value = "/save-edit-microlesson-static", method = RequestMethod.POST)
    public String saveMicroLessonStaticFile(Model model, ModelMap modelMap,
                                            @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                            @ModelAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER) QPalXEMicroLesson qPalXEMicroLesson,
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
        ELearningMediaContent staticELearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXEMicroLessonVO);

        if(staticELearningMediaContent == null || staticELearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Failed to upload MicroLesson narration file...");
            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), "Error occurred on fileupload, check the file type and content");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-static");
        } else {
            // Delete the previously uploaded narration file IF exists
            if (qPalXEMicroLesson.getStaticELearningMediaContent() != null) {
                LOGGER.info("Deleting previously saved MicroLesson static file: {}", qPalXEMicroLesson.getStaticELearningMediaContent());
                ieLearningStaticContentService.deleteELearningMediaContent(qPalXEMicroLesson.getStaticELearningMediaContent());
            }

            // Update new narration file on micro lesson
            qPalXEMicroLesson.setStaticELearningMediaContent(staticELearningMediaContent);
            qPalXEMicroLessonVO.setStaticELearningMediaContent(staticELearningMediaContent);

            // Save updated QPalXMicroLesson here with new narration file in case user abandons edit
            iqPalXEMicroLessonService.updateAndSaveQPalXEMicroLesson(qPalXEMicroLesson, qPalXEMicroLessonVO);
            modelMap.addAttribute("MicroLessonSavedAndUpdated", Boolean.TRUE);

            LOGGER.info("Upload of narration file was succeful, setting ELearningMediaContent for narration");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("edit-micro-lesson-interactivee");
        }
    }


    @RequestMapping(value = "/save-edit-microlesson-interactive", method = RequestMethod.POST)
    public String saveMicroLessonInteractiveFile(Model model, SessionStatus status,
                                                 @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                                 @ModelAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER) QPalXEMicroLesson qPalXEMicroLesson,
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
        ELearningMediaContent interactiveELearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(multipartFile, qPalXEMicroLessonVO);

        if(interactiveELearningMediaContent == null || interactiveELearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
            LOGGER.warn("Failed to upload MicroLesson narration file...");
            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), "Error occurred on fileupload, check the file type and content");
            return ContentRootE.Content_Admin_Micro_Lessons.getContentRootPagePath("add-micro-lesson-interactive");
        } else {
            // Delete the previously uploaded narration file IF exists
            if (qPalXEMicroLesson.getInteractiveELearningMediaContent() != null) {
                LOGGER.info("Deleting previously saved MicroLesson static file: {}", qPalXEMicroLesson.getInteractiveELearningMediaContent());
                ieLearningStaticContentService.deleteELearningMediaContent(qPalXEMicroLesson.getInteractiveELearningMediaContent());
            }

            // Update new narration file on micro lesson
            qPalXEMicroLesson.setInteractiveELearningMediaContent(interactiveELearningMediaContent);
            qPalXEMicroLessonVO.setInteractiveELearningMediaContent(interactiveELearningMediaContent);

            // Save updated QPalXMicroLesson here with new narration file in case user abandons edit
            iqPalXEMicroLessonService.updateAndSaveQPalXEMicroLesson(qPalXEMicroLesson, qPalXEMicroLessonVO);

            LOGGER.info("Upload of narration file was succeful, setting ELearningMediaContent for narration, saving final MicroLesson details...");
            iqPalXEMicroLessonService.createAndSaveQPalXEMicroLesson(qPalXEMicroLessonVO);

            status.setComplete();

            // redirect back to view all micro lessons in this Lesson.
            String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXEMicroLessonVO.getQPalXELessonID();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
            return "";
        }
    }

    @RequestMapping(value = "/complete-microlesson-edit-wizard", method = RequestMethod.POST)
    public void completeMicroLessonCreateWizard(Model model, SessionStatus status,
                                                @ModelAttribute("MicroLessonSavedAndUpdated") Boolean microLessonUpdated,
                                                @ModelAttribute(QPalXEMicroLesson.CLASS_ATTRIBUTE_IDENTIFIER) QPalXEMicroLesson qPalXEMicroLesson,
                                                @ModelAttribute(QPalXEMicroLessonVO.CLASS_ATTRIBUTE) QPalXEMicroLessonVO qPalXEMicroLessonVO,
                                               HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Completing MicroLesson creation wizard....");

        if (microLessonUpdated == Boolean.FALSE) {
            // Save updated QPalXMicroLesson here with new narration file in case user abandons edit
            iqPalXEMicroLessonService.updateAndSaveQPalXEMicroLesson(qPalXEMicroLesson, qPalXEMicroLessonVO);
        }

        status.setComplete();
        String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXEMicroLessonVO.getQPalXELessonID();
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

    @RequestMapping(value = "/exit-microlesson-edit", method = RequestMethod.GET)
    public void exitMicroLessonCreateWizard(Model model, SessionStatus status,
                                            @ModelAttribute("SelectedQPalXELesson") QPalXELesson qPalXELesson,
                                            HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Canceling edit of MicroLesson wizard for Lesson: {}", qPalXELesson.getId());
        String targetURL = "/view-admin-qpalx-micro-elessons?qpalxELessonID=" + qPalXELesson.getId();
        status.setComplete();
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

}
