package com.quaza.solutions.qpalx.elearning.web.zone.content.student.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLessonQuizStatistics;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.scorable.QuestionBankItem;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.statistics.AdaptiveLessonStatistics;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.statistics.AdaptiveMicroLessonStatistics;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.TutorialLevelCalendar;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.scorable.IQuestionBankService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.statistics.IAdaptiveLessonStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.statistics.IAdaptiveMicroLessonStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXELessonService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.ITutorialLevelCalendarService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IStudentInfoOverviewPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.ITutorialLevelCalendarPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.LessonsAdminAttributesE;
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
public class StudentLessonController {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultELearningCourseService")
    private IELearningCourseService ieLearningCourseService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXELessonService")
    private IQPalXELessonService iqPalXELessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXEMicroLessonService")
    private IQPalXEMicroLessonService iqPalXEMicroLessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLessonStatisticsService")
    private IAdaptiveLessonStatisticsService iAdaptiveLessonStatisticsService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveMicroLessonStatisticsService")
    private IAdaptiveMicroLessonStatisticsService iAdaptiveMicroLessonStatisticsService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultTutorialLevelCalendarService")
    private ITutorialLevelCalendarService iTutorialLevelCalendarService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QuestionBankService")
    private IQuestionBankService iQuestionBankService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizStatisticsService")
    private IAdaptiveLearningQuizStatisticsService iAdaptiveLearningQuizStatisticsService;

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


    @RequestMapping(value = "/view-course-lessons", method = RequestMethod.GET)
    public String viewAdaptiveLessons(final Model model, @RequestParam("eLearningCourseID") String eLearningCourseID, @RequestParam("tutorialLevelID") String tutorialLevelID) {
        LOGGER.info("Finding and displaying all lessons for courseID: {} and tutorialLevelID: {}", eLearningCourseID, tutorialLevelID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        Long cId = NumberUtils.toLong(eLearningCourseID);
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(cId);
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);
        iStudentInfoOverviewPanelService.addStudentInfoOverviewWithCourse(model, eLearningCourse);

        // Find the current default TutorialLevelCalendar based on the selected value
        Long tId = NumberUtils.toLong(tutorialLevelID);
        TutorialLevelCalendar selectedTutorialLevelCalendar = iTutorialLevelCalendarService.findByID(tId);
        List<AdaptiveLessonStatistics> adaptiveLessonStatisticsList = iAdaptiveLessonStatisticsService.findAdaptiveLessonStatisticsByCourseIDAndTutorialLevel(optionalUser.get(), selectedTutorialLevelCalendar, eLearningCourse);
        model.addAttribute(LessonsAdminAttributesE.QPalXELessons.toString(), adaptiveLessonStatisticsList);

        // Add all attributes required for Student tutorial level calendar panel.  By Default we load only first term if no calendar is specified
        iTutorialLevelCalendarPanelService.addCalendarPanelInfo(model, selectedTutorialLevelCalendar);

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Add Lessons display attributes
        model.addAttribute(AdaptiveLearningDisplayAttributeE.LessonsDisplayEnabled.toString(), Boolean.TRUE.toString());

        return ContentRootE.Student_Adaptive_Learning.getContentRootPagePath("lesson-display-page");
    }

    @RequestMapping(value = "/view-micro-lessons", method = RequestMethod.GET)
    public String viewAdaptiveMicroLessons(final Model model, @RequestParam("eLessonID") String eLessonID, @RequestParam("tutorialLevelID") String tutorialLevelID) {
        LOGGER.info("Finding and displaying all micro lessons for eLessonID: {}", eLessonID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        Long id = NumberUtils.toLong(eLessonID);
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(id);
        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);
        iStudentInfoOverviewPanelService.addStudentInfoOverviewWithLesson(model, qPalXELesson);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);

        // Find all micro lessons for this lesson
        List<AdaptiveMicroLessonStatistics> adaptiveMicroLessonStatisticsList = iAdaptiveMicroLessonStatisticsService.findAdaptiveMicroLessonStatisticsByLessonAndCourse(qPalXELesson, optionalUser.get());
        model.addAttribute(LessonsAdminAttributesE.QPalXEMicroLessons.toString(), adaptiveMicroLessonStatisticsList);

        if (adaptiveMicroLessonStatisticsList !=null && adaptiveMicroLessonStatisticsList.size() > 0) {
            List<AdaptiveLessonQuizStatistics> adaptiveLessonQuizStatisticsList = iAdaptiveLearningQuizStatisticsService.findStudentQuizzesStatisticsForLesson(optionalUser.get(), qPalXELesson.getId());

            if(adaptiveLessonQuizStatisticsList.size() == 1) {
                AdaptiveLessonQuizStatistics adaptiveLessonQuizStatistics = adaptiveLessonQuizStatisticsList.get(0);
                if(adaptiveLessonQuizStatistics.getAdaptiveLearningQuizID().longValue() > 0) {
                    model.addAttribute(LessonsAdminAttributesE.AdaptiveLearningQuizzes.toString(), adaptiveLessonQuizStatisticsList);
                }
            } else {
                model.addAttribute(LessonsAdminAttributesE.AdaptiveLearningQuizzes.toString(), adaptiveLessonQuizStatisticsList);
            }
        }

        // Find the current default TutorialLevelCalendar based on the selected value
        Long tId = NumberUtils.toLong(tutorialLevelID);
        iTutorialLevelCalendarPanelService.addCalendarPanelInfo(model, tId);

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Add Micro-Lessons display attributes
        model.addAttribute(AdaptiveLearningDisplayAttributeE.MicroLessonsDisplayEnabled.toString(), Boolean.TRUE.toString());

        // Add randomly selected question bank item
        QuestionBankItem questionBankItem = iQuestionBankService.findRandomQuestionBankItem(qPalXELesson);
        model.addAttribute(CurriculumDisplayAttributeE.RandomQuestionBankItem.toString(), questionBankItem);

        LOGGER.info("Returning micro lessons display page ==> micro-lesson-display");
        return ContentRootE.Student_Adaptive_Learning.getContentRootPagePath("micro-lesson-display");
    }

    @RequestMapping(value = "/view-micro-lessons-with-quiz", method = RequestMethod.GET)
    public String viewAdaptiveMicroLessonsByMicroLesson(final Model model, @RequestParam("eLessonID") String eLessonID, @RequestParam("tutorialLevelID") String tutorialLevelID, @RequestParam("microLessonID") String microLessonID) {
        LOGGER.info("Finding and displaying all micro lessons for eLessonID: {}.  Displaying quizzes for microLessonID: {}", eLessonID, microLessonID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        Long id = NumberUtils.toLong(eLessonID);
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(id);
        iStudentInfoOverviewPanelService.addStudentInfoOverviewWithLesson(model, qPalXELesson);

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);

        // Find all micro lessons for this lesson
        List<AdaptiveMicroLessonStatistics> adaptiveMicroLessonStatisticsList = iAdaptiveMicroLessonStatisticsService.findAdaptiveMicroLessonStatisticsByLessonAndCourse(qPalXELesson, optionalUser.get());
        model.addAttribute(LessonsAdminAttributesE.QPalXEMicroLessons.toString(), adaptiveMicroLessonStatisticsList);

        Long microLessonIDToLoad = NumberUtils.toLong(microLessonID);
        List<AdaptiveLessonQuizStatistics> adaptiveLessonQuizStatisticsList = iAdaptiveLearningQuizStatisticsService.findMicroLessonStudentQuizStatistics(optionalUser.get(), microLessonIDToLoad);
        model.addAttribute(LessonsAdminAttributesE.AdaptiveLearningQuizzes.toString(), adaptiveLessonQuizStatisticsList);

        // Find the current default TutorialLevelCalendar based on the selected value
        Long tId = NumberUtils.toLong(tutorialLevelID);
        iTutorialLevelCalendarPanelService.addCalendarPanelInfo(model, tId);

        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);

        // Add Micro-Lessons display attributes
        model.addAttribute(AdaptiveLearningDisplayAttributeE.MicroLessonsDisplayEnabled.toString(), Boolean.TRUE.toString());

        // Add randomly selected question bank item
        QuestionBankItem questionBankItem = iQuestionBankService.findRandomQuestionBankItem(qPalXELesson);
        model.addAttribute(CurriculumDisplayAttributeE.RandomQuestionBankItem.toString(), questionBankItem);

        LOGGER.info("Returning micro lessons display page ==> micro-lesson-display");
        return ContentRootE.Student_Adaptive_Learning.getContentRootPagePath("micro-lesson-display");
    }


}
