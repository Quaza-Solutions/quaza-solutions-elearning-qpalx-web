package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.AdaptiveProficiencyRanking;
import com.quaza.solutions.qpalx.elearning.domain.subjectmatter.proficiency.ProficiencyRankingScaleE;
import com.quaza.solutions.qpalx.elearning.domain.subjectmatter.proficiency.ProficiencyScoreRangeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveScoreDisplayAttributesE;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.service.AdaptiveLearningScoreChartDisplayPanel")
public class AdaptiveLearningScoreChartDisplayPanel implements IAdaptiveLearningScoreChartDisplayPanel {


    public static final String SPRING_BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.AdaptiveLearningScoreChartDisplayPanel";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdaptiveLearningScoreChartDisplayPanel.class);


    @Override
    public void addEmptyLearningScoreChartDisplayPanel(Model model) {
        LOGGER.debug("Adding empty Adaptive Learning Chart score details...");
        model.addAttribute(AdaptiveScoreDisplayAttributesE.AdaptiveLearningChartScore.toString(), new Double(0));
        model.addAttribute(AdaptiveScoreDisplayAttributesE.AdaptiveLearningChartInverseScore.toString(), new Double(0));
    }

    @Override
    public void addLearningScoreChartDisplayPanel(Model model, double adaptiveChartScore) {
        double inverseAdaptiveChartScore = adaptiveChartScore / 100;
        LOGGER.debug("Adding score chart display AdaptiveLearningChartScore: {} AdaptiveLearningChartInverseScore: {}", adaptiveChartScore, inverseAdaptiveChartScore);
        model.addAttribute(AdaptiveScoreDisplayAttributesE.AdaptiveLearningChartScore.toString(), new Double(adaptiveChartScore));
        model.addAttribute(AdaptiveScoreDisplayAttributesE.AdaptiveLearningChartInverseScore.toString(), new Double(inverseAdaptiveChartScore));
    }

    @Override
    public void addCurriculumProficiency(Model model, AdaptiveProficiencyRanking adaptiveProficiencyRanking) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(adaptiveProficiencyRanking, "adaptiveProficiencyRanking cannot be null");

        ProficiencyRankingScaleE proficiencyRankingScaleE = adaptiveProficiencyRanking.getProficiencyRankingScaleE();
        ProficiencyScoreRangeE proficiencyScoreRangeE = proficiencyRankingScaleE.getProficiencyScoreRangeE();

        double inverseScore = getInverseProficiencyScore(proficiencyRankingScaleE.getProficiencyRanking());
        String scoreRangeText = proficiencyScoreRangeE.getScoreRange().getMinimum() + " - " + proficiencyScoreRangeE.getScoreRange().getMaximum() + "%";
        model.addAttribute(AdaptiveScoreDisplayAttributesE.AdaptiveLearningChartScore.toString(), new Double(proficiencyRankingScaleE.getProficiencyRanking()));
        model.addAttribute(AdaptiveScoreDisplayAttributesE.AdaptiveLearningChartInverseScore.toString(), new Double(inverseScore));
        model.addAttribute(AdaptiveScoreDisplayAttributesE.ProficiencyRankingScaleE.toString(), proficiencyRankingScaleE);
        model.addAttribute(AdaptiveScoreDisplayAttributesE.ProficiencyScoreRangeE.toString(), scoreRangeText);

    }


    private double getInverseProficiencyScore(double score) {
        // highest proficiency is 10 so divide by 10
        return score / 10;
    }


}
