package com.quaza.solutions.qpalx.elearning.web.service.quiz.maker;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;

/**
 * @author manyce400
 */
public interface IClassicQuizQuestionMaker {

    public ClassicQuizMakerError persistQuizQuestionDetails(AdaptiveLearningQuiz adaptiveLearningQuiz, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO, Integer questionOrder);

}
