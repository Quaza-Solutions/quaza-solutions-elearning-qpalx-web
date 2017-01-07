package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface IAdaptiveLearningScoreChartDisplayPanel {

    public void addEmptyLearningScoreChartDisplayPanel(Model model);

    public void addLearningScoreChartDisplayPanel(Model model, double adaptiveChartScore);
}
