package com.quaza.solutions.qpalx.elearning.web.service.quiz.maker;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.*;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.repository.IAdaptiveLearningQuizQuestionAnswerRepository;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.repository.IAdaptiveLearningQuizQuestionRepository;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.AdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * @author manyce400
 */
@Service(ClassicQuizQuestionMaker.BEAN_NAME)
public class ClassicQuizQuestionMaker implements IClassicQuizQuestionMaker {



    @Autowired
    @Qualifier(AdaptiveLearningQuizService.BEAN_NAME)
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    private IAdaptiveLearningQuizQuestionAnswerRepository iAdaptiveLearningQuizQuestionAnswerRepository;

    @Autowired
    private IAdaptiveLearningQuizQuestionRepository iAdaptiveLearningQuizQuestionRepository;

    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.quiz.maker.ClassicQuizQuestionMaker";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClassicQuizQuestionMaker.class);


    @Override
    public ClassicQuizMakerError persistQuizQuestionDetails(AdaptiveLearningQuiz adaptiveLearningQuiz, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO, Integer questionOrder) {
        Assert.notNull(adaptiveLearningQuiz, "adaptiveLearningQuiz cannot be null");
        Assert.notNull(iAdaptiveLearningQuizQuestionVO, "adaptiveLearningQuiz cannot be null");
        Assert.notNull(questionOrder, "adaptiveLearningQuiz cannot be null");

        if(iAdaptiveLearningQuizQuestionVO.getID() == null) {
            return persistNewQuizQuestion(adaptiveLearningQuiz, iAdaptiveLearningQuizQuestionVO, questionOrder);
        } else {
            return modifyQuizQuestion(adaptiveLearningQuiz, iAdaptiveLearningQuizQuestionVO, questionOrder);
        }
    }

    protected ClassicQuizMakerError persistNewQuizQuestion(AdaptiveLearningQuiz adaptiveLearningQuiz, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO, Integer questionOrder) {
        LOGGER.info("Saving new details for quiz question: {}", iAdaptiveLearningQuizQuestionVO.getQuestionTitle());

        AdaptiveLearningQuizQuestion questionToPersist = AdaptiveLearningQuizQuestion.builder()
                .questionOrder(questionOrder)
                .questionTitle(iAdaptiveLearningQuizQuestionVO.getQuestionTitle())
                .questionFeedBack(iAdaptiveLearningQuizQuestionVO.getQuestionFeedBack())
                .quizQuestionMultiMedia(iAdaptiveLearningQuizQuestionVO.getQuizQuestionMultiMedia())
                .adaptiveLearningQuizQuestionTypeE(iAdaptiveLearningQuizQuestionVO.getAdaptiveLearningQuizQuestionTypeE())
                .entryDate(new DateTime())
                .adaptiveLearningQuiz(adaptiveLearningQuiz)
                .build();

        iAdaptiveLearningQuizService.saveQuizQuestion(questionToPersist);
        iAdaptiveLearningQuizQuestionVO.setID(questionToPersist.getId());

        // Do a new save of all quiz questions
        persistNewQuizQuestionAnswers(questionToPersist, iAdaptiveLearningQuizQuestionVO);
        return ClassicQuizMakerError.NO_ERRORS;
    }

    protected ClassicQuizMakerError persistNewQuizQuestionAnswers(AdaptiveLearningQuizQuestion questionPersisted, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO) {
        Set<IAdaptiveLearningQuizQuestionAnswerVO> adaptiveLearningQuizQuestionAnswerVOS = iAdaptiveLearningQuizQuestionVO.getIAdaptiveLearningQuizQuestionAnswerVOs();

        int questionAnswerOrder = 1;
        for(IAdaptiveLearningQuizQuestionAnswerVO iAdaptiveLearningQuizQuestionAnswerVO : adaptiveLearningQuizQuestionAnswerVOS) {
            LOGGER.info("Building and saving new Quiz Question Answer: {} For Quiz: {}", iAdaptiveLearningQuizQuestionAnswerVO.getQuizQuestionAnswerText(), questionPersisted.getQuestionTitle());
            AdaptiveLearningQuizQuestionAnswer questionAnswerToPersist = AdaptiveLearningQuizQuestionAnswer.builder()
                    .questionAnswerOrder(questionAnswerOrder)
                    .quizQuestionAnswerText(iAdaptiveLearningQuizQuestionAnswerVO.getQuizQuestionAnswerText())
                    .quizQuestionAnswerMultiMedia(iAdaptiveLearningQuizQuestionAnswerVO.getQuizQuestionAnswerMultiMedia())
                    .isCorrectAnswer(iAdaptiveLearningQuizQuestionAnswerVO.isCorrectAnswer())
                    .entryDate(new DateTime())
                    .adaptiveLearningQuizQuestion(questionPersisted)
                    .build();

            questionAnswerOrder++;

            // Save all the Quiz Question Answers and update the VO object with persisted ID
            iAdaptiveLearningQuizQuestionAnswerRepository.save(questionAnswerToPersist);
            iAdaptiveLearningQuizQuestionAnswerVO.setID(questionAnswerToPersist.getId());
        }

        return ClassicQuizMakerError.NO_ERRORS;
    }

    protected ClassicQuizMakerError modifyQuizQuestion(AdaptiveLearningQuiz adaptiveLearningQuiz, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO, Integer questionOrder) {
        LOGGER.info("Updating and persisting existing Adaptive Quiz details with id: {}", iAdaptiveLearningQuizQuestionVO.getID());
        AdaptiveLearningQuizQuestion persistedAdaptiveLearningQuizQuestion = iAdaptiveLearningQuizQuestionRepository.findOne(iAdaptiveLearningQuizQuestionVO.getID());

        persistedAdaptiveLearningQuizQuestion.setQuestionTitle(iAdaptiveLearningQuizQuestionVO.getQuestionTitle());
        persistedAdaptiveLearningQuizQuestion.setQuestionFeedBack(iAdaptiveLearningQuizQuestionVO.getQuestionFeedBack());
        persistedAdaptiveLearningQuizQuestion.setModifyDate(DateTime.now());
        iAdaptiveLearningQuizService.saveQuizQuestion(persistedAdaptiveLearningQuizQuestion);

        // Update the existing quiz question answers with new answer details
        updateExistingQuestionAnswers(persistedAdaptiveLearningQuizQuestion, iAdaptiveLearningQuizQuestionVO);
        return ClassicQuizMakerError.NO_ERRORS;
    }

    protected ClassicQuizMakerError updateExistingQuestionAnswers(AdaptiveLearningQuizQuestion persistedAdaptiveLearningQuizQuestion, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO) {
        Set<IAdaptiveLearningQuizQuestionAnswerVO> adaptiveLearningQuizQuestionAnswerVOS = iAdaptiveLearningQuizQuestionVO.getIAdaptiveLearningQuizQuestionAnswerVOs();

        // Convert the Question Answer value objects to an Array, we will use this to update the already persisted question answers
        IAdaptiveLearningQuizQuestionAnswerVO[] quizQuestionAnswersArray = adaptiveLearningQuizQuestionAnswerVOS.toArray(new IAdaptiveLearningQuizQuestionAnswerVO[adaptiveLearningQuizQuestionAnswerVOS.size()]);
        LOGGER.info("Quiz Question Answers array: {}", quizQuestionAnswersArray);

        // Get all the already persisted Question Answers that will need to be updated
        Set<AdaptiveLearningQuizQuestionAnswer> persistedQuizQuestionAnswers = persistedAdaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();

        int valueObjectIndex = 0;
        for (AdaptiveLearningQuizQuestionAnswer quizQuestionAnswerToUpdate : persistedQuizQuestionAnswers) {

            IAdaptiveLearningQuizQuestionAnswerVO quizQuestionAnswer = quizQuestionAnswersArray[valueObjectIndex];

            AdaptiveLearningQuizQuestionAnswer savedQuestionAnswer = iAdaptiveLearningQuizQuestionAnswerRepository.findOne(quizQuestionAnswerToUpdate.getId());
            LOGGER.info("Updating Quiz Question Answer with ID: {}", savedQuestionAnswer.getId());
            savedQuestionAnswer.setQuizQuestionAnswerText(quizQuestionAnswer.getQuizQuestionAnswerText());
            savedQuestionAnswer.setQuizQuestionAnswerMultiMedia(quizQuestionAnswer.getQuizQuestionAnswerMultiMedia());
            savedQuestionAnswer.setCorrectAnswer(quizQuestionAnswer.isCorrectAnswer());
            iAdaptiveLearningQuizQuestionAnswerRepository.save(savedQuestionAnswer);

            valueObjectIndex++;
        }

        return ClassicQuizMakerError.NO_ERRORS;
    }


}
