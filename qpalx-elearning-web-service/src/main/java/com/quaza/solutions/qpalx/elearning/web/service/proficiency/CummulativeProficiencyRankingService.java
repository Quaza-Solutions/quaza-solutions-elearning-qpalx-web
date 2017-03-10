package com.quaza.solutions.qpalx.elearning.web.service.proficiency;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.AdaptiveProficiencyRanking;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.ProficiencyRankingCompuationResult;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.statistics.StudentOverallProgressStatistics;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.IAdaptiveProficiencyRankingAnalyticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.IAdaptiveProficiencyRankingService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.statistics.IStudentOverallProgressStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author manyce400
 */
public class CummulativeProficiencyRankingService implements ICummulativeProficiencyRankingService {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledELearningCurriculumService")
    private IELearningCurriculumService ieLearningCurriculumService;


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StudentOverallProgressStatisticsService")
    private IStudentOverallProgressStatisticsService iStudentOverallProgressStatisticsService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultAdaptiveProficiencyRankingService")
    private IAdaptiveProficiencyRankingService iAdaptiveProficiencyRankingService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CumulativeAdaptiveProficiencyRankingAnalyticsService")
    private IAdaptiveProficiencyRankingAnalyticsService iAdaptiveProficiencyRankingAnalyticsService;




    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CummulativeProficiencyRankingService.class);


    @Transactional
    @Override
    public List<ProficiencyRankingCompuationResult> computeAndRecordStudentProficienciesByCurriculumType(QPalXUser qPalXUser, CurriculumType curriculumType) {
        Assert.notNull(qPalXUser, "qpalxUser cannot be null");
        Assert.notNull(curriculumType, "curriculumType cannot be null");

        LOGGER.info("Calculating and recording CurriculumType: {} proficiency rankings for student: {}", curriculumType, qPalXUser.getEmail());
        List<ProficiencyRankingCompuationResult> proficiencyRankingCompuationResults = new ArrayList<>();

        // Find all Student overall progress statistics across all the CurriculumType passed as argument
        List<StudentOverallProgressStatistics> studentOverallProgressStatisticsList = iStudentOverallProgressStatisticsService.getStudentOverallProgressStatistics(qPalXUser, curriculumType);

        for(StudentOverallProgressStatistics studentOverallProgressStatistics : studentOverallProgressStatisticsList) {
            boolean completionPercentGreaterThan50 = isCompletionPercentGreaterThan50(studentOverallProgressStatistics);
            ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(studentOverallProgressStatistics.getCurriculumID());

            if (completionPercentGreaterThan50) {
                AdaptiveProficiencyRanking currentAdaptiveProficiencyRanking = iAdaptiveProficiencyRankingService.findCurrentStudentAdaptiveProficiencyRankingForCurriculum(qPalXUser, eLearningCurriculum);
                AdaptiveProficiencyRanking newAdaptiveProficiencyRanking = iAdaptiveProficiencyRankingAnalyticsService.calculateStudentProficiencyWithProgress(qPalXUser, studentOverallProgressStatistics);

                ProficiencyRankingCompuationResult proficiencyRankingCompuationResult = ProficiencyRankingCompuationResult.newResultAdaptiveProficiencyRanking(newAdaptiveProficiencyRanking);
                proficiencyRankingCompuationResults.add(proficiencyRankingCompuationResult);

                // Close out the current existing AdaptiveProficiencyRanking for this curriculum
                closeOutCurrentAdaptiveProficiencyRanking(currentAdaptiveProficiencyRanking);

                // Save and record this new AdaptiveProficiencyRanking for this curriculum
                iAdaptiveProficiencyRankingService.save(newAdaptiveProficiencyRanking);
            } else {
                LOGGER.info("Progress completion percent in ELearningCurriculum: {} is less than 30% cannot calculate adequate proficiency", studentOverallProgressStatistics.getCurriculumName());
                ProficiencyRankingCompuationResult proficiencyRankingCompuationResult = buildProficiencyRankingCompuationResult(studentOverallProgressStatistics);
                proficiencyRankingCompuationResults.add(proficiencyRankingCompuationResult);
            }
        }

        return proficiencyRankingCompuationResults;
    }

    boolean isCompletionPercentGreaterThan50(StudentOverallProgressStatistics studentOverallProgressStatistics) {
        double completionPercent = studentOverallProgressStatistics.getTotalCurriculumCompletionPercent();
        return completionPercent >= 50.0;
    }

    ProficiencyRankingCompuationResult buildProficiencyRankingCompuationResult(StudentOverallProgressStatistics studentOverallProgressStatistics) {
        String reason = new StringBuffer()
                .append("Your Completion % in CurriculumType: ")
                .append(studentOverallProgressStatistics.getCurriculumType())
                .append(" Curriculum: ")
                .append(studentOverallProgressStatistics.getCurriculumName())
                .append(" is lower than 50%")
                .toString();
        ProficiencyRankingCompuationResult proficiencyRankingCompuationResult = ProficiencyRankingCompuationResult.newResultNoAdaptiveProficiencyRanking(reason);
        return proficiencyRankingCompuationResult;
    }

    void closeOutCurrentAdaptiveProficiencyRanking(AdaptiveProficiencyRanking currentAdaptiveProficiencyRanking) {
        LOGGER.info("In order to record new, closing out currentAdaptiveProficiencyRanking: {}", currentAdaptiveProficiencyRanking);
        currentAdaptiveProficiencyRanking.setProficiencyRankingEndDateTime(new DateTime());
        iAdaptiveProficiencyRankingService.save(currentAdaptiveProficiencyRanking);
    }

}
