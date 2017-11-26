package com.quaza.solutions.qpalx.elearning.web.service.quiz.editor;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IEditableAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;

/**
 * @author manyce400
 */
public interface IClassicQuizEditor {

    public void updateQuizWithEdits(AdaptiveLearningQuiz adaptiveLearningQuiz, AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO);

    public void updateQuizQuestionWithEdits(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion, IEditableAdaptiveLearningQuizQuestionVO iEditableAdaptiveLearningQuizQuestionVO);


}
