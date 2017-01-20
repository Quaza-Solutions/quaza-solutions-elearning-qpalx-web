package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
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


    @RequestMapping(value = "/add-curriculum-course", method = RequestMethod.GET)
    public String addCurriculumCourse(final Model model,
                                      @RequestParam("curriculumID") String curriculumID,
                                      HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Add new ELearningCurriculum with curriculumID: {} requested", curriculumID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        if(optionalUser.get().getUserType() == QPalxUserTypeE.CONTENT_DEVELOPER) {
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
