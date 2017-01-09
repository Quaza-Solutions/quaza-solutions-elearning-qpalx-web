package com.quaza.solutions.qpalx.elearning.web.service.user.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizResultVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveQuizQuestionStudentResponseVO;
import org.springframework.ui.ModelMap;

import java.util.Map;

/**
 * @author manyce400
 */
public interface IStudentQuizQuestionService {

    public void addAdaptiveQuizQuestionScoreVO(ModelMap modelMap, AdaptiveQuizQuestionStudentResponseVO adaptiveQuizQuestionStudentResponseVO);

    public AdaptiveQuizQuestionStudentResponseVO findAdaptiveQuizQuestionStudentResponse(ModelMap modelMap, Integer quizQuestionModelID);

    public Map<Integer, AdaptiveLearningQuizQuestion> getAdaptiveQuizQuestionsModel(AdaptiveLearningQuiz adaptiveLearningQuiz);

    public AdaptiveLearningQuizResultVO calculateAdaptiveQuizResults(Map<Integer, AdaptiveQuizQuestionStudentResponseVO> questionResponseMap, Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap);

}
