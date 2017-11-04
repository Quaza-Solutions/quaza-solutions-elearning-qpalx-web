package com.quaza.solutions.qpalx.elearning.web.zone.content.student.performance;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.algorithm.ProficiencyAlgorithmExecutionInfo;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.TutorialLevelCalendar;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.algorithm.IAdaptiveProficiencyAlgorithm;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.algorithm.QuizCompletionAdaptiveProficiencyAlgorithm;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.DefaultELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.ITutorialLevelCalendarService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IStudentInfoOverviewPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.ITutorialLevelCalendarPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.GlobalControlPerformanceOverviewPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IGlobalControlPerformanceOverviewPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class CoursePerformanceOverviewController {


    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(QuizCompletionAdaptiveProficiencyAlgorithm.BEAN_NAME)
    private IAdaptiveProficiencyAlgorithm iAdaptiveProficiencyAlgorithm;

    @Autowired
    @Qualifier(DefaultELearningCourseService.BEAN_NAME)
    private IELearningCourseService ieLearningCourseService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultTutorialLevelCalendarService")
    private ITutorialLevelCalendarService iTutorialLevelCalendarService;

    @Autowired
    @Qualifier(GlobalControlPerformanceOverviewPanelService.BEAN_NAME)
    private IGlobalControlPerformanceOverviewPanelService iGlobalControlPerformanceOverviewPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.TutorialLevelCalendarPanelService")
    private ITutorialLevelCalendarPanelService iTutorialLevelCalendarPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.StudentInfoOverviewPanelService")
    private IStudentInfoOverviewPanelService iStudentInfoOverviewPanelService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CoursePerformanceOverviewController.class);


    @RequestMapping(value = "/improve-my-course-performance", method = RequestMethod.GET)
    public String getImproveMyCoursePerformanceRecommendations(final Model model, @RequestParam("eLearningCourseID") Long eLearningCourseID, @RequestParam("tutorialLevelID") Long tutorialLevelID) {
        LOGGER.info("Finding and displaying all course performance recommendations courseID: {} and tutorialLevelID: {}", eLearningCourseID, tutorialLevelID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(eLearningCourseID);
        TutorialLevelCalendar tutorialLevelCalendar = iTutorialLevelCalendarService.findByID(tutorialLevelID);

        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);
        iStudentInfoOverviewPanelService.addStudentInfoOverviewWithCourse(model, eLearningCourse);

        ProficiencyAlgorithmExecutionInfo proficiencyAlgorithmExecutionInfo = iAdaptiveProficiencyAlgorithm.executeAlgorithm(optionalUser.get(), eLearningCourse, tutorialLevelCalendar);
        System.out.println("proficiencyAlgorithmExecutionInfo = " + proficiencyAlgorithmExecutionInfo);
        model.addAttribute(ProficiencyAlgorithmExecutionInfo.CLASS_ATTRIBUTE, proficiencyAlgorithmExecutionInfo);

        // Calculate Student's realtime global performance in this course.
        iGlobalControlPerformanceOverviewPanelService.addELearningCourseGlobalPerformance(model, eLearningCourse, optionalUser.get());

        // Add all attributes required for Student tutorial level calendar panel.  By Default we load only first term if no calendar is specified
        iTutorialLevelCalendarPanelService.addCalendarPanelInfo(model, tutorialLevelCalendar);

        return ContentRootE.Student_Adaptive_Learning.getContentRootPagePath("course-performance-recomendations");
    }

}