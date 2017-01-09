package com.quaza.solutions.qpalx.elearning.web.service.user.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionAnswer;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizResultVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveQuizQuestionStudentResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.service.StudentQuizQuestionService")
public class StudentQuizQuestionService implements IStudentQuizQuestionService {




    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentQuizQuestionService.class);


    @Override
    public void addAdaptiveQuizQuestionScoreVO(ModelMap modelMap, AdaptiveQuizQuestionStudentResponseVO adaptiveQuizQuestionStudentResponseVO) {
        Assert.notNull(modelMap, "modelMap cannot be null");
        Assert.notNull(adaptiveQuizQuestionStudentResponseVO, "adaptiveQuizQuestionStudentResponseVO cannot be null");
        Assert.notNull(adaptiveQuizQuestionStudentResponseVO.getQuizQuestionModelID(), "Question Model ID cannot be null on student response");

        Integer questionModelID = adaptiveQuizQuestionStudentResponseVO.getQuizQuestionModelID();

        // Mapping of Quiz Question Model ID to Student question responses
        Map<Integer, AdaptiveQuizQuestionStudentResponseVO> questionResponseMap = (Map<Integer, AdaptiveQuizQuestionStudentResponseVO>) modelMap.get(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestionScores.toString());

        LOGGER.debug("Adding Quiz question model ID: {} to mapping of scores", questionModelID);
        if(questionResponseMap == null) {
            questionResponseMap = new HashMap<>();
            modelMap.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestionScores.toString(), questionResponseMap);
        }

        questionResponseMap.put(questionModelID, adaptiveQuizQuestionStudentResponseVO);
    }

    @Override
    public AdaptiveQuizQuestionStudentResponseVO findAdaptiveQuizQuestionStudentResponse(ModelMap modelMap, Integer quizQuestionModelID) {
        Assert.notNull(modelMap, "modelMap cannot be null");
        Assert.notNull(quizQuestionModelID, "quizQuestionModelID cannot be null");

        // Mapping of Quiz Question Model ID to Student question responses
        Map<Integer, AdaptiveQuizQuestionStudentResponseVO> questionResponseMap = (Map<Integer, AdaptiveQuizQuestionStudentResponseVO>) modelMap.get(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestionScores.toString());

        if(questionResponseMap == null) {
            System.out.println("questionResponseMap is null this does not make sense");
        }

        return questionResponseMap.get(quizQuestionModelID);
    }

    @Override
    public Map<Integer, AdaptiveLearningQuizQuestion> getAdaptiveQuizQuestionsModel(AdaptiveLearningQuiz adaptiveLearningQuiz) {
        Assert.notNull(adaptiveLearningQuiz, "adaptiveLearningQuiz cannot be null");
        LOGGER.debug("Building quiz questions model for AdaptiveLearningQuiz: {}", adaptiveLearningQuiz.getQuizTitle());

        Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap = new HashMap<>();

        Set<AdaptiveLearningQuizQuestion> adaptiveLearningQuizQuestions = adaptiveLearningQuiz.getAdaptiveLearningQuizQuestions();
        
        int count = 1;
        for(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion : adaptiveLearningQuizQuestions) {
            questionModelMap.put(count, adaptiveLearningQuizQuestion);
            count++;
        }

        return questionModelMap;
    }

    @Override
    public AdaptiveLearningQuizResultVO calculateAdaptiveQuizResults(Map<Integer, AdaptiveQuizQuestionStudentResponseVO> questionResponseMap, Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap) {
        Assert.notNull(questionResponseMap, "questionResponseMap cannot be null");
        Assert.notNull(questionModelMap, "questionModelMap cannot be null");

        AdaptiveLearningQuizResultVO adaptiveLearningQuizResultVO = new AdaptiveLearningQuizResultVO(questionModelMap.size());

        Collection<AdaptiveQuizQuestionStudentResponseVO> adaptiveQuizQuestionStudentResponseVOCollection = questionResponseMap.values();

        for(AdaptiveQuizQuestionStudentResponseVO adaptiveQuizQuestionStudentResponseVO : adaptiveQuizQuestionStudentResponseVOCollection) {
            // Look up the question for Student user's quiz answer and generate a score
            AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion = questionModelMap.get(adaptiveQuizQuestionStudentResponseVO.getQuizQuestionModelID());

            // append question feedback
            adaptiveLearningQuizResultVO.appendQuestionFeedBack(adaptiveLearningQuizQuestion.getQuestionFeedBack());

            // calculate question score details
            calculateQuizQuestionScore(adaptiveLearningQuizQuestion, adaptiveQuizQuestionStudentResponseVO, adaptiveLearningQuizResultVO);
        }

        return adaptiveLearningQuizResultVO;
    }


    public void calculateQuizQuestionScore(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion, AdaptiveQuizQuestionStudentResponseVO adaptiveQuizQuestionStudentResponseVO, AdaptiveLearningQuizResultVO adaptiveLearningQuizResultVO) {
        Set<AdaptiveLearningQuizQuestionAnswer> adaptiveLearningQuizQuestionAnswers = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();

        // Find the correct quiz question answer and check to see if it matches student score answer
        for (AdaptiveLearningQuizQuestionAnswer adaptiveLearningQuizQuestionAnswer : adaptiveLearningQuizQuestionAnswers) {
            if(adaptiveLearningQuizQuestionAnswer.isCorrectAnswer()) {
                boolean correctAnswerMatchesUserAnswer = adaptiveLearningQuizQuestionAnswer.getQuizQuestionAnswerText().equals(adaptiveQuizQuestionStudentResponseVO.getUserSelectedAnswerText());

                if(correctAnswerMatchesUserAnswer) {
                    adaptiveLearningQuizResultVO.incrementTotalQuestionsCorrect();
                }

            }
        }
    }

}
