package com.quaza.solutions.qpalx.elearning.web.service.quiz.maker;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import org.springframework.ui.ModelMap;

/**
 * @author manyce400
 */
public interface IClassicQuizMakerSessionTracker {

    public void startAdaptiveQuizCreation(ModelMap modelMap, IHierarchicalLMSContent iHierarchicalLMSContent, IAdaptiveLearningQuizVO iAdaptiveLearningQuizVO);



}
