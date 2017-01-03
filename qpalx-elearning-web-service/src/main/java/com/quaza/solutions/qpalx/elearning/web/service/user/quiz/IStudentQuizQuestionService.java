package com.quaza.solutions.qpalx.elearning.web.service.user.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizResultVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveQuizQuestionStudentResponseVO;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

/**
 * @author manyce400
 */
public interface IStudentQuizQuestionService {

    public void addAdaptiveQuizQuestionScoreVO(ModelMap modelMap, AdaptiveQuizQuestionStudentResponseVO adaptiveQuizQuestionStudentResponseVO);

    public Map<Integer, AdaptiveLearningQuizQuestion> getAdaptiveQuizQuestionsModel(AdaptiveLearningQuiz adaptiveLearningQuiz);

    public AdaptiveLearningQuizResultVO calculateAdaptiveQuizResults(List<AdaptiveQuizQuestionStudentResponseVO> adaptiveQuizQuestionStudentResponseVOS, Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap);

}
