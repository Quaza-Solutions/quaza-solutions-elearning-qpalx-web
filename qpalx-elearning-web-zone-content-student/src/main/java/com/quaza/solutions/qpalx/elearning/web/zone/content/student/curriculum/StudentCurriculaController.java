package com.quaza.solutions.qpalx.elearning.web.zone.content.student.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourseActivity;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.TutorialLevelCalendar;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseActivityService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.ITutorialLevelCalendarService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IStudentInfoOverviewPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.ITutorialLevelCalendarPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.TutorialCalendarPanelE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class StudentCurriculaController {


    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledELearningCurriculumService")
    private IELearningCurriculumService ieLearningCurriculumService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultELearningCourseService")
    private IELearningCourseService ieLearningCourseService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultELearningCourseActivityService")
    private IELearningCourseActivityService ieLearningCourseActivityService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultTutorialLevelCalendarService")
    private ITutorialLevelCalendarService iTutorialLevelCalendarService;


    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.TutorialLevelCalendarPanelService")
    private ITutorialLevelCalendarPanelService iTutorialLevelCalendarPanelService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.StudentInfoOverviewPanelService")
    private IStudentInfoOverviewPanelService iStudentInfoOverviewPanelService;



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentCurriculaController.class);

    @RequestMapping(value = "/curriculum-courses", method = RequestMethod.GET)
    public String displayAllCurriculumCourses(final Model model, @RequestParam("curriculumID") String curriculumID) {
        LOGGER.info("Finding all courses for curriculumID: {}", curriculumID);

        // Find the current default TutorialLevelCalendar based on the current month
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        Optional<TutorialLevelCalendar> selectedTutorialLevelCalendar = iTutorialLevelCalendarService.findCurrentCalendarByTutorialLevel(optionalUser.get());
        model.addAttribute(TutorialCalendarPanelE.SelectedTutorialCalendar.toString(), selectedTutorialLevelCalendar.get());

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        addSelectedCurriculumInfoToResponse(model, curriculumID);
        return ContentRootE.Student_Home.getContentRootPagePath("curriciculum-courses");
    }



    @RequestMapping(value = "/qpalx-course-details", method = RequestMethod.GET)
    public String displayQPalXCourseActivities(final Model model, @RequestParam("qCourseID") String qCourseID) {
        LOGGER.info("Retrieving all learning activities in courseID: {}", qCourseID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        Long id = NumberUtils.toLong(qCourseID);
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(id);

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Find the current default TutorialLevelCalendar based on the current month
        Optional<TutorialLevelCalendar> selectedTutorialLevelCalendar = iTutorialLevelCalendarService.findCurrentCalendarByTutorialLevel(optionalUser.get());
        List<ELearningCourseActivity> eLearningCourseActivities = ieLearningCourseActivityService.findCourseAcitivitiesByCalendarAndCourse(selectedTutorialLevelCalendar.get(), eLearningCourse);
        model.addAttribute("SelectedELearningCourseActivities", eLearningCourseActivities);

        // Add all attributes required for Student tutorial level calendar panel.  By Default we load only first term if no calendar is specified
        iTutorialLevelCalendarPanelService.addCalendarPanelInfo(model, selectedTutorialLevelCalendar.get());

        addSelectedCourseInfoToResponse(model, qCourseID);
        addSelectedCurriculumInfoToResponse(model, eLearningCourse.geteLearningCurriculum().getId().toString());
        return ContentRootE.Student_Home.getContentRootPagePath("course-activities");
    }

    @RequestMapping(value = "/qpalx-course-by-calendar-details", method = RequestMethod.GET)
    public String displayQPalXCourseActivitiesWithCalendar(final Model model, @RequestParam("qCourseID") String qCourseID, @RequestParam("tutorialLevelCalendarID") String tutorialLevelCalendarID) {
        LOGGER.info("Retrieving all learning activities with courseID: {} and tutorialLevelCalendarID: {}", qCourseID, tutorialLevelCalendarID);

        Long courseID = NumberUtils.toLong(qCourseID);
        Long calendarID = NumberUtils.toLong(tutorialLevelCalendarID);
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(courseID);

        TutorialLevelCalendar selectedTutorialLevelCalendar = iTutorialLevelCalendarService.findByID(calendarID);

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Add all attributes required for Student tutorial level calendar panel.
        iTutorialLevelCalendarPanelService.addCalendarPanelInfo(model, calendarID);

        // Add all course activities for tutorial level and ELearning course
        List<ELearningCourseActivity> eLearningCourseActivities = ieLearningCourseActivityService.findCourseAcitivitiesByCalendarAndCourse(selectedTutorialLevelCalendar, eLearningCourse);
        model.addAttribute("SelectedELearningCourseActivities", eLearningCourseActivities);

        addSelectedCourseInfoToResponse(model, qCourseID);
        addSelectedCurriculumInfoToResponse(model, eLearningCourse.geteLearningCurriculum().getId().toString());
        return ContentRootE.Student_Home.getContentRootPagePath("course-activities");
    }

    @RequestMapping(value = "/play-qcourse-video", method = RequestMethod.GET)
    public String playQCourseActivity(final Model model, @RequestParam("courseActivityID") String courseActivityID) {
        LOGGER.info("Accessing Q Course acitivity with courseActivityID: {}", courseActivityID);

        Long id = NumberUtils.toLong(courseActivityID);
        ELearningCourseActivity eLearningCourseActivity = ieLearningCourseActivityService.findByID(id);
        ELearningCourse eLearningCourse = eLearningCourseActivity.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        String studentTutorialGradeID = eLearningCurriculum.getStudentTutorialGrade().getId().toString();
        String curriculumType = eLearningCurriculum.getCurriculumType().toString();

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        addSelectedCurriculumInfoToResponse(model, eLearningCourseActivity.geteLearningCourse().geteLearningCurriculum().getId().toString());
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCurriculum.toString(), eLearningCurriculum);
        model.addAttribute("SelectedELearningCourse", eLearningCourse);
        model.addAttribute("SelectedELearningCourseActivity", eLearningCourseActivity);
        model.addAttribute("SelectedELearningCourseActivityFile", eLearningCourseActivity.geteLearningMediaContent().getELearningMediaFile());
        model.addAttribute("SelectedMediaContentType", eLearningCourseActivity.geteLearningMediaContent().getELearningMediaType());
        return ContentRootE.Student_Home.getContentRootPagePath("video-widget");
    }

    private void addSelectedCurriculumInfoToResponse(final Model model, String curriculumID) {
        Long id = NumberUtils.toLong(curriculumID);
        ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(id);
        iStudentInfoOverviewPanelService.addStudentInfoOverviewWithCurriculum(model, eLearningCurriculum);

        // Find all E-Learning courses for this curriculum
        List<ELearningCourse> eLearningCourses =  ieLearningCourseService.findByELearningCurriculum(eLearningCurriculum);
        model.addAttribute("CurriculumELearningCourses", eLearningCourses);
        model.addAttribute("CurriculumType", eLearningCurriculum.getCurriculumType().toString());
    }

    private void addSelectedCourseInfoToResponse(final Model model, String courseID) {
        Long id = NumberUtils.toLong(courseID);
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(id);
        model.addAttribute("SelectedELearningCourse", eLearningCourse);
        model.addAttribute("ELearningCourseParentCurriculum", eLearningCourse.geteLearningCurriculum());
    }

}
