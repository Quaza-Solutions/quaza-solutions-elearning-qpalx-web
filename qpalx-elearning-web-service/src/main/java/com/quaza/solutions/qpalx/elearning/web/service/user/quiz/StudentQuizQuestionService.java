package com.quaza.solutions.qpalx.elearning.web.service.user.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.service.StudentQuizQuestionService")
public class StudentQuizQuestionService implements IStudentQuizQuestionService {




    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentQuizQuestionService.class);


    @Override
    public Map<Integer, AdaptiveLearningQuizQuestion> getAdaptiveQuizQuestionsModel(AdaptiveLearningQuiz adaptiveLearningQuiz) {
        Assert.notNull(adaptiveLearningQuiz, "adaptiveLearningQuiz cannot be null");
        LOGGER.debug("Building quiz questions model for AdaptiveLearningQuiz: {}", adaptiveLearningQuiz.getQuizTitle());

        Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap = new HashMap<>();

        Set<AdaptiveLearningQuizQuestion> adaptiveLearningQuizQuestions = adaptiveLearningQuiz.getAdaptiveLearningQuizQuestions();
        if(adaptiveLearningQuizQuestions != null) {
            System.out.println("adaptiveLearningQuizQuestions.size() = " + adaptiveLearningQuizQuestions.size());
        }
        
        int count = 1;
        for(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion : adaptiveLearningQuizQuestions) {
            questionModelMap.put(count, adaptiveLearningQuizQuestion);
            count++;
        }

        return questionModelMap;
    }

}
