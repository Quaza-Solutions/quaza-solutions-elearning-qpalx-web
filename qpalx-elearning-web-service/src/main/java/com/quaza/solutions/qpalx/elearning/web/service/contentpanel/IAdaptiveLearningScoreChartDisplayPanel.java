package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.AdaptiveProficiencyRanking;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface IAdaptiveLearningScoreChartDisplayPanel {

    public void addEmptyLearningScoreChartDisplayPanel(Model model);

    public void addLearningScoreChartDisplayPanel(Model model, double adaptiveChartScore);

    public void addCurriculumProficiency(Model model, AdaptiveProficiencyRanking adaptiveProficiencyRanking);
}
