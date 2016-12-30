package com.quaza.solutions.qpalx.elearning.web.service.user.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;

import java.util.Map;

/**
 * @author manyce400
 */
public interface IStudentQuizQuestionService {

    public Map<Integer, AdaptiveLearningQuizQuestion> getAdaptiveQuizQuestionsModel(AdaptiveLearningQuiz adaptiveLearningQuiz);

}
