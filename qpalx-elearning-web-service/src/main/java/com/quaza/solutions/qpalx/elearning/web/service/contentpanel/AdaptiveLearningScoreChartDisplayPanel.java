package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveScoreDisplayAttributesE;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.service.AdaptiveLearningScoreChartDisplayPanel")
public class AdaptiveLearningScoreChartDisplayPanel implements IAdaptiveLearningScoreChartDisplayPanel {


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

}
