package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourseActivity;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.service.institutions.IQPalXEducationalInstitutionService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseActivityService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IContentAdminTutorialGradePanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdminTutorialGradePanelE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.ELearningCourseWebVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class StudentCurriculaAdminController {




    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledELearningCurriculumService")
    private IELearningCurriculumService ieLearningCurriculumService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXTutorialService")
    private IQPalXTutorialService iqPalXTutorialService;

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
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultELearningCourseActivityService")
    private IELearningCourseActivityService ieLearningCourseActivityService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService")
    private IELearningStaticContentService ieLearningStaticContentService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.web.ContentAdminTutorialGradePanelService")
    private IContentAdminTutorialGradePanelService contentAdminTutorialGradePanelService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentCurriculaAdminController.class);



    @RequestMapping(value = "/curriculum-by-tutorialgrade", method = RequestMethod.GET)
    public String viewCurriculumByTutorialLevel(final Model model, @RequestParam("tutorialGradeID") String tutorialGradeID, @RequestParam("curriculumType") String curriculumType) {
        LOGGER.info("Curriculum for tutorialGradeID:> {} and curriculumType:> {} requested", tutorialGradeID, curriculumType);

        // Find all Core and Elective curriculum for tutorial grade
        CurriculumType curriculumTypeE = CurriculumType.valueOf(curriculumType);
        StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(NumberUtils.toLong(tutorialGradeID));
        List<ELearningCurriculum> eLearningCurricula = ieLearningCurriculumService.findAllCurriculumByTutorialGradeAndType(curriculumTypeE, studentTutorialGrade);

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Add all attributes required for content admin tutorial panel
        contentAdminTutorialGradePanelService.addDisplayPanelAttributes(model, Boolean.FALSE, Boolean.FALSE, tutorialGradeID, curriculumType);

        // Add attributes required for page
        model.addAttribute("ELearningCurricula", eLearningCurricula);
        return ContentRootE.Content_Admin_Home.getContentRootPagePath("home");
    }


    @RequestMapping(value = "/view-admin-curriculum-courses", method = RequestMethod.GET)
    public String displayAllCurriculumCourses(final Model model, @RequestParam("curriculumID") String curriculumID) {
        LOGGER.info("Retrieving and displaying all Admin Curriculum courses for curriculumID:> {}", curriculumID);
        Long id = NumberUtils.toLong(curriculumID);
        ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(id);
        String studentTutorialGradeID = eLearningCurriculum.getStudentTutorialGrade().getId().toString();
        String curriculumType = eLearningCurriculum.getCurriculumType().toString();

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        // Add all attributes required for content admin tutorial panel
        contentAdminTutorialGradePanelService.addDisplayPanelAttributes(model, Boolean.TRUE, Boolean.FALSE, studentTutorialGradeID, curriculumType);

        // Add all attributes for Admin view ELearning curriculum courses page
        List<ELearningCourse> eLearningCourses =  ieLearningCourseService.findByELearningCurriculum(eLearningCurriculum);
        model.addAttribute("SelectedELearningCurriculum", eLearningCurriculum);
        model.addAttribute("CurriculumELearningCourses", eLearningCourses);
        return ContentRootE.Content_Admin_Home.getContentRootPagePath("view-courses");
    }

    @RequestMapping(value = "/view-admin-course-activities", method = RequestMethod.GET)
    public String viewAdminCourseActivities(final Model model, @RequestParam("eLearningCourseID") String eLearningCourseID) {
        LOGGER.info("All ELearningCourse activities for eLearningCourseID:> {} requested", eLearningCourseID);

        // Lookup the ELearning course
        Long courseID = NumberUtils.toLong(eLearningCourseID);
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(courseID);
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        String studentTutorialGradeID = eLearningCurriculum.getStudentTutorialGrade().getId().toString();
        String curriculumType = eLearningCurriculum.getCurriculumType().toString();

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Add all attributes required for content admin tutorial panel
        contentAdminTutorialGradePanelService.addDisplayPanelAttributes(model, Boolean.FALSE, Boolean.TRUE, studentTutorialGradeID, curriculumType);

        // find all the ELearning activities for this course
        List<ELearningCourseActivity> eLearningCourseActivities = ieLearningCourseActivityService.findELearningCourseAcitivitiesByCourse(eLearningCourse);
        model.addAttribute("SelectedELearningCurriculum", eLearningCurriculum);
        model.addAttribute("SelectedELearningCourse", eLearningCourse);
        model.addAttribute("ELearningCourseActivities", eLearningCourseActivities);
        return ContentRootE.Content_Admin_Home.getContentRootPagePath("view-course-activities");
    }

    @RequestMapping(value = "/view-course-activity", method = RequestMethod.GET)
    public String viewAdminCourseActivity(final Model model, @RequestParam("activityID") String activityID) {
        LOGGER.info("Loading ELearning Course activity with activityID: {}", activityID);
        Long id = NumberUtils.toLong(activityID);

        ELearningCourseActivity eLearningCourseActivity = ieLearningCourseActivityService.findByID(id);
        ELearningCourse eLearningCourse = eLearningCourseActivity.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        String studentTutorialGradeID = eLearningCurriculum.getStudentTutorialGrade().getId().toString();
        String curriculumType = eLearningCurriculum.getCurriculumType().toString();

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Add all attributes required for content admin tutorial panel
        contentAdminTutorialGradePanelService.addDisplayPanelAttributes(model, Boolean.FALSE, Boolean.FALSE, studentTutorialGradeID, curriculumType);

        // find all the ELearning activities for this course
        List<ELearningCourseActivity> eLearningCourseActivities = ieLearningCourseActivityService.findELearningCourseAcitivitiesByCourse(eLearningCourse);
        model.addAttribute("SelectedELearningCurriculum", eLearningCurriculum);
        model.addAttribute("SelectedELearningCourse", eLearningCourse);
        model.addAttribute("SelectedELearningCourseActivity", eLearningCourseActivity);
        model.addAttribute("SelectedELearningCourseActivityFile", eLearningCourseActivity.geteLearningMediaContent().getELearningMediaFile());
        model.addAttribute("SelectedMediaContentType", eLearningCourseActivity.geteLearningMediaContent().getELearningMediaType());
        return ContentRootE.Content_Admin_Home.getContentRootPagePath("video-widget");
    }


    @RequestMapping(value = "/add-curriculum-course", method = RequestMethod.GET)
    public String addCurriculumCourse(final Model model,
                                      @RequestParam("curriculumID") String curriculumID,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Add new ELearningCurriculum with curriculumID: {} requested", curriculumID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        if(optionalUser.get().getUserType() == QPalxUserTypeE.CONTENT_DEVELOPER || optionalUser.get().getUserType() == QPalxUserTypeE.ADMINISTRATOR) {
            Long id = NumberUtils.toLong(curriculumID);
            ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(id);
            List<QPalXEducationalInstitution> qPalXEducationalInstitutions = iqPalXEducationalInstitutionService.findAll();

            // Add all attributes required for User information panel
            qPalXUserInfoPanelService.addUserInfoAttributes(model);

            // Add all attributes required for add elearning course page
            model.addAttribute("SelectedELearningCurriculum", eLearningCurriculum);
            model.addAttribute("QPalXEducationalInstitutions", qPalXEducationalInstitutions);
            model.addAttribute(AdminTutorialGradePanelE.ELearningCourseWebVO.toString(), new ELearningCourseWebVO());

            // Add error message if present
            Object errorMessage = request.getSession().getAttribute("ELearningCourseAddError");
            if(errorMessage != null) {
                model.addAttribute("AddCourseErrorSet", "true");
                model.addAttribute("ErrorMessage", errorMessage.toString());
                request.getSession().removeAttribute("ELearningCourseAddError");
            }
            return ContentRootE.Content_Admin_Home.getContentRootPagePath("add-elearning-course");
        }

        LOGGER.info("Currently logged in user does not have Content Admin rights, returning to home page....");
        return ContentRootE.Student_Home.getContentRootPagePath("selected-curriculum");
    }

//    @RequestMapping(value = "/add-curriculum-course-activity", method = RequestMethod.GET)
//    public String addCurriculumCourseActivity(final Model model,
//                                              @RequestParam("eLearningCourseID") String eLearningCourseID,
//                                              HttpServletRequest request, HttpServletResponse response) {
//        LOGGER.info("Add new ELearning course activity page requested for eLearningCourseID: {}", eLearningCourseID);
//        Long id = NumberUtils.toLong(eLearningCourseID);
//
//        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(id);
//
//        // Add all attributes required for User information panel
//        qPalXUserInfoPanelService.addUserInfoAttributes(model);
//
//        // Add error message if present
//        Object errorMessage = request.getSession().getAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString());
//        if(errorMessage != null) {
//            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), errorMessage.toString());
//            request.getSession().removeAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString());
//        }
//
//        // Add all attributes required for add elearning course page
//        model.addAttribute("SelectedELearningCourse", eLearningCourse);
//        model.addAttribute("ProficiencyRankings", ProficiencyRankingScaleE.values());
//        model.addAttribute("LearningActivities", LearningActivityE.values());
//        model.addAttribute(AdminTutorialGradePanelE.ELearningCourseActivityWebVO.toString(), new ELearningCourseActivityWebVO());
//        return ContentRootE.Content_Admin_Home.getContentRootPagePath("add-elearning-course-activity");
//    }


    @RequestMapping(value = "/save-elearning-course", method = RequestMethod.POST)
    public void createELearningCourse(Model model,
                                      HttpServletRequest request, HttpServletResponse response,
                                      @ModelAttribute("ELearningCourseWebVO") ELearningCourseWebVO eLearningCourseWebVO) {
        LOGGER.info("Attempting to create new ELearningCourse from eLearningCourseWebVO:> {}", eLearningCourseWebVO);
        //String elearningCurriculum = request.getParameter("educationalInstitutionID");
        ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(eLearningCourseWebVO.getELearningCurriculumID());


        // Make sure that this course hasn't all ready been created for this curriculum
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseNameAndELearningCurriculum(eLearningCourseWebVO.getCourseName(), eLearningCurriculum);
        if(eLearningCourse == null) {
            ieLearningCourseService.createELearningCourse(eLearningCourseWebVO);
            String targetURL = "/view-admin-curriculum-courses?curriculumID=" + eLearningCurriculum.getId();
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        } else {
            LOGGER.warn("Content Admin user attempted to create an already existing course:> {} returning back to Add Elearning course", eLearningCourseWebVO.getCourseName());
            String targetURL = "/add-curriculum-course?curriculumID=" + eLearningCurriculum.getId();
            String errorMessage = eLearningCourseWebVO.getCourseName() + " ELearningCourse already created.";
            request.getSession().setAttribute("ELearningCourseAddError", errorMessage);
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        }
    }

//    @RequestMapping(value = "/save-elearning-course-activity", method = RequestMethod.POST)
//    public void saveELearningCourseActivity(Model model,
//                                            HttpServletRequest request, HttpServletResponse response,
//                                            @RequestParam("eLearningCourseID") String eLearningCourseID,
//                                            @ModelAttribute("ELearningCourseWebVO") ELearningCourseActivityWebVO eLearningCourseActivityWebVO,
//                                            @RequestParam("file") MultipartFile multipartFile) {
//        LOGGER.info("Attempting to create new ELearningCourse Activity from eLearningCourseWebVO:> {}", eLearningCourseActivityWebVO);
//        Long courseID = NumberUtils.toLong(eLearningCourseID);
//
//        // Upload file and create the ELearningMediaContent
//        ELearningMediaContent eLearningMediaContent = iFileUploadUtil.uploadELearningCourseActivityContent(multipartFile, eLearningCourseActivityWebVO.getLearningActivityE());
//
//        if(eLearningMediaContent == null) {
//            LOGGER.warn("Selected ELearning Media content could not be uploaded.  Check selected file content.");
//            String targetURL = "/add-curriculum-course-activity?eLearningCourseID=" + courseID;
//            String errorMessage = "Failed to upload file: Check the contents of the file";
//            request.getSession().setAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), errorMessage);
//            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
//        } else if(eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
//            LOGGER.warn("Uploaded course activity media content file is currently not supported...");
//            String targetURL = "/add-curriculum-course-activity?eLearningCourseID=" + courseID;
//            String errorMessage = "Uploaded file is not supported: Only Files of type(MP4, SWF) supported";
//            request.getSession().setAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), errorMessage);
//            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
//        } else {
//            LOGGER.info("ELearningMediaContent was succesfully uploaded, building and saving ELearningContentActivity details....");
//            eLearningCourseActivityWebVO.setELearningMediaContent(eLearningMediaContent);
//            ieLearningCourseActivityService.buildNew(eLearningCourseActivityWebVO);
//
//            // On Succesful save redirect back to course activities page.
//            String targetURL = "/view-admin-course-activities?eLearningCourseID=" + courseID;
//            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
//        }
//    }

    @RequestMapping(value = "/delete-elearning-course", method = RequestMethod.GET)
    public void deleteELearningCourse(final Model model,
                                        HttpServletRequest request, HttpServletResponse response,
                                        @RequestParam("eLearningCourseID") String eLearningCourseID, @RequestParam("curriculumID") String curriculumID) {
        LOGGER.info("ELearningCourse with id:> {} deletion has been requested", eLearningCourseID);
        Long id = NumberUtils.toLong(eLearningCourseID);
        ieLearningCourseService.deleteELearningCourse(id);
        String targetURL = "/view-admin-curriculum-courses?curriculumID=" + curriculumID;
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }



}
