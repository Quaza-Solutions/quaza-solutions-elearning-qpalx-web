package com.quaza.solutions.qpalx.elearning.web.service.admin.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionAnswer;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author manyce400
 */
@Service(AdaptiveLearningQuizAdminService.BEAN_NAME)
public class AdaptiveLearningQuizAdminService implements IAdaptiveLearningQuizAdminService {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizService2")
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService")
    private IELearningStaticContentService ieLearningStaticContentService;

    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.admin.AdaptiveLearningQuizAdminService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdaptiveLearningQuizAdminService.class);


    @Override
    public void deleteAdaptiveLearningQuiz(AdaptiveLearningQuiz adaptiveLearningQuiz) {
        Assert.notNull(adaptiveLearningQuiz, "adaptiveLearningQuiz cannot be null");
        LOGGER.info("Attempting to delete quiz with ID: {} title: {}", adaptiveLearningQuiz.getId(), adaptiveLearningQuiz.getQuizTitle());

        // Delete the actual Quiz from DB
        iAdaptiveLearningQuizService.delete(adaptiveLearningQuiz);

        // Delete all media content associated with this Quiz.  This means deleting all questions and answers multimedia files
        deleteAllAdaptiveQuizQuestionsMedia(adaptiveLearningQuiz);
    }


    private void deleteAllAdaptiveQuizQuestionsMedia(AdaptiveLearningQuiz adaptiveLearningQuiz) {
        List<ELearningMediaContent> eLearningMediaContentList = new ArrayList<>();
        Set<AdaptiveLearningQuizQuestion> adaptiveLearningQuizQuestionSet = adaptiveLearningQuiz.getAdaptiveLearningQuizQuestions();

        for(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion : adaptiveLearningQuizQuestionSet) {
            // Add Quiz question media
            ELearningMediaContent quizQuestionMultiMedia = adaptiveLearningQuizQuestion.getQuizQuestionMultiMedia();

            if(quizQuestionMultiMedia != null) {
                LOGGER.info("Deleting Quiz Question multiMedia: {}", quizQuestionMultiMedia);
                ieLearningStaticContentService.deleteELearningMediaContent(quizQuestionMultiMedia);
            }

            // Get all multi media content for this Quiz question answers...
           deleteAllAdaptiveQuizQuestionAnswersMedia(adaptiveLearningQuizQuestion);
        }
    }

    private void deleteAllAdaptiveQuizQuestionAnswersMedia(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion) {
        List<ELearningMediaContent> eLearningMediaContentList = new ArrayList<>();
        Set<AdaptiveLearningQuizQuestionAnswer> adaptiveLearningQuizQuestionAnswerSet = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();

        for(AdaptiveLearningQuizQuestionAnswer adaptiveLearningQuizQuestionAnswer : adaptiveLearningQuizQuestionAnswerSet) {
            ELearningMediaContent quizQuestionAnswerMultiMedia = adaptiveLearningQuizQuestionAnswer.getQuizQuestionAnswerMultiMedia();
            if(quizQuestionAnswerMultiMedia != null) {
                LOGGER.info("Deleting Quiz Question Answer multiMedia: {}", quizQuestionAnswerMultiMedia);
                ieLearningStaticContentService.deleteELearningMediaContent(quizQuestionAnswerMultiMedia);
            }
        }
    }
}
