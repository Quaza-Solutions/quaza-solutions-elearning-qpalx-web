package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.AdaptiveProficiencyRanking;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.ProficiencyRankingTriggerTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.DefaultAdaptiveProficiencyRankingService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.IAdaptiveProficiencyRankingService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.microlesson.IMicroLessonPerformanceMonitorService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.microlesson.MicroLessonPerformanceMonitorService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.domain.GlobalPerformance;
import org.apache.commons.lang3.Range;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(GlobalControlPerformanceOverviewPanelService.BEAN_NAME)
public class GlobalControlPerformanceOverviewPanelService implements IGlobalControlPerformanceOverviewPanelService {



    @Autowired
    @Qualifier(DefaultAdaptiveProficiencyRankingService.SPRING_BEAN_NAME)
    private IAdaptiveProficiencyRankingService iAdaptiveProficiencyRankingService;

    @Autowired
    @Qualifier(MicroLessonPerformanceMonitorService.BEAN_NAME)
    private IMicroLessonPerformanceMonitorService iMicroLessonPerformanceMonitorService;


    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.user.GlobalControlPerformanceOverviewPanelService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GlobalControlPerformanceOverviewPanelService.class);


    @Override
    public void addPerformanceOverviewInCurriculum(Model model, ELearningCurriculum eLearningCurriculum, QPalXUser studentQPalxUser) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(eLearningCurriculum, "eLearningCurriculum cannot be null");
        Assert.notNull(studentQPalxUser, "studentQPalxUser cannot be null");

        LOGGER.debug("Building performance overview panel data for User: {} in Curriculumm: {}", studentQPalxUser.getEmail(), eLearningCurriculum.getCurriculumName());

        // Find Adaptive ProficiencyRanking for this user
        AdaptiveProficiencyRanking adaptiveProficiencyRanking = iAdaptiveProficiencyRankingService.findCurrentStudentAdaptiveProficiencyRankingForCurriculum(studentQPalxUser, eLearningCurriculum);
        model.addAttribute("AdaptiveProficiencyRanking", adaptiveProficiencyRanking);
        if(adaptiveProficiencyRanking.getProficiencyRankingTriggerTypeE() != ProficiencyRankingTriggerTypeE.ENROLMENT) {
            // IF ProficiencyRankingTriggerTypeE is not Enrolment this ranking is not default and user has put i some work.  We can display this ranking
            Range<Double> scoreRange = adaptiveProficiencyRanking.getProficiencyRankingScaleE().getProficiencyScoreRangeE().getScoreRange();
            String rangeStr = scoreRange.getMinimum() + "% - " + scoreRange.getMaximum() + "%";
            model.addAttribute("ProficiencyScoreRange", rangeStr);
        }
    }

    @Override
    public void addELearningCurriclumGlobalPerformance(Model model, ELearningCurriculum eLearningCurriculum, QPalXUser studentQPalxUser) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(eLearningCurriculum, "eLearningCurriculum cannot be null");
        Assert.notNull(studentQPalxUser, "studentQPalxUser cannot be null");

        LOGGER.debug("Finding and returning Students current AdaptiveProficiency Global Performance Ranking in eLearningCurriculum: {}", eLearningCurriculum.getCurriculumName());

        // Find Adaptive ProficiencyRanking for this user
        AdaptiveProficiencyRanking adaptiveProficiencyRanking = iAdaptiveProficiencyRankingService.findCurrentStudentAdaptiveProficiencyRankingForCurriculum(studentQPalxUser, eLearningCurriculum);

        // IF AdaptiveProficiencyRanking was calculated today reflect
        boolean proficiencyUpdatedToday = adaptiveProficiencyRanking.getProficiencyRankingEffectiveDateTime().toLocalDate().equals(LocalDate.now());
        System.out.println("proficiencyUpdatedToday = " + proficiencyUpdatedToday);
        model.addAttribute("ProficiencyUpdatedToday", Boolean.valueOf(proficiencyUpdatedToday).toString());

        GlobalPerformance globalPerformance = GlobalPerformance.newInstance("Beginner", adaptiveProficiencyRanking.getProficiencyRankingScaleE());
        model.addAttribute(GlobalPerformance.CLASS_ATTRIBUTE, globalPerformance);
    }

    @Override
    public void addELearningCourseGlobalPerformance(Model model, ELearningCourse eLearningCourse, QPalXUser studentQPalxUser) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(eLearningCourse, "eLearningCourse cannot be null");
        Assert.notNull(studentQPalxUser, "studentQPalxUser cannot be null");

        LOGGER.info("Calculating realtime global performance for Student: {} in Course: {}", studentQPalxUser.getEmail(), eLearningCourse.getCourseName());

        AdaptiveProficiencyRanking adaptiveProficiencyRanking = iMicroLessonPerformanceMonitorService.calculateAdaptiveProficiencyRanking(studentQPalxUser, eLearningCourse);

        // Build Global Performance
        GlobalPerformance globalPerformance = GlobalPerformance.newInstance("Beginner", adaptiveProficiencyRanking.getProficiencyRankingScaleE());
        model.addAttribute(GlobalPerformance.CLASS_ATTRIBUTE, globalPerformance);
    }
}
