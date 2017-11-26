package com.quaza.solutions.qpalx.elearning.web.service.quiz.editor;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.*;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.repository.IAdaptiveLearningQuizQuestionAnswerRepository;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.AdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.ELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Set;

/**
 * @author manyce400
 */
@Service(ClassicQuizEditor.BEAN_NAME)
public class ClassicQuizEditor implements IClassicQuizEditor {


    @Autowired
    @Qualifier(AdaptiveLearningQuizService.BEAN_NAME)
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    private IAdaptiveLearningQuizQuestionAnswerRepository iAdaptiveLearningQuizQuestionAnswerRepository;

    @Autowired
    @Qualifier(ELearningStaticContentService.BEAN_NAME)
    private IELearningStaticContentService ieLearningStaticContentService;


    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.quiz.editor.ClassicQuizEditor";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClassicQuizEditor.class);


    @Override
    @Transactional
    public void updateQuizWithEdits(AdaptiveLearningQuiz adaptiveLearningQuiz, AdaptiveLearningQuizWebVO iEditableQuiz) {
        Assert.notNull(adaptiveLearningQuiz, "adaptiveLearningQuiz cannot be null");
        Assert.notNull(iEditableQuiz, "iEditableQuiz cannot be null");
        LOGGER.debug("Updating quiz with ID: {} with edits", adaptiveLearningQuiz.getId());
        adaptiveLearningQuiz.setModifyDate(DateTime.now());
        adaptiveLearningQuiz.setQuizTitle(iEditableQuiz.getQuizTitle());
        adaptiveLearningQuiz.setQuizDescription(iEditableQuiz.getQuizDescription());
        iAdaptiveLearningQuizService.saveQuiz(adaptiveLearningQuiz);
    }

    @Override
    @Transactional
    public void updateQuizQuestionWithEdits(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion, IEditableAdaptiveLearningQuizQuestionVO iEditableAdaptiveLearningQuizQuestionVO) {
        Assert.notNull(adaptiveLearningQuizQuestion, "adaptiveLearningQuizQuestion cannot be null");
        Assert.notNull(iEditableAdaptiveLearningQuizQuestionVO, "iEditableAdaptiveLearningQuizQuestionVO cannot be null");
       // Assert.isTrue(iEditableAdaptiveLearningQuizQuestionVO.isQuizEditValid(), "Quiz edit has to be valid");
        LOGGER.debug("Updating quiz question with ID: {} with new edits...", adaptiveLearningQuizQuestion.getId());

        // Update basic question information from edits
        updateBasicQuestionInfoWithEdits(adaptiveLearningQuizQuestion, iEditableAdaptiveLearningQuizQuestionVO);

        // Update all question answer details
        updateQuizQuestionAnswersWithEdits(adaptiveLearningQuizQuestion, iEditableAdaptiveLearningQuizQuestionVO);
    }

    @Transactional
    protected void updateBasicQuestionInfoWithEdits(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion, IEditableAdaptiveLearningQuizQuestionVO iEditableAdaptiveLearningQuizQuestionVO) {
        adaptiveLearningQuizQuestion.setQuestionTitle(iEditableAdaptiveLearningQuizQuestionVO.getQuestionTitle());
        adaptiveLearningQuizQuestion.setQuestionFeedBack(iEditableAdaptiveLearningQuizQuestionVO.getQuestionFeedBack());
        adaptiveLearningQuizQuestion.setModifyDate(DateTime.now());
        iAdaptiveLearningQuizService.saveQuizQuestion(adaptiveLearningQuizQuestion);
    }

    @Transactional
    protected void updateQuizQuestionAnswersWithEdits(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion, IEditableAdaptiveLearningQuizQuestionVO iEditableAdaptiveLearningQuizQuestionVO) {
        int correctAnswerIndex = iEditableAdaptiveLearningQuizQuestionVO.getCorrectAnswerIndex();

        // Currently persisted QuizQuestion answers in order
        Set<AdaptiveLearningQuizQuestionAnswer> adaptiveLearningQuizQuestionAnswers = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();

        // Edited quiz question answers in order
        List<IAdaptiveLearningQuizQuestionAnswerVO> questionAnswersWithEdits = iEditableAdaptiveLearningQuizQuestionVO.getQuestionAnswersWithEdits();

        int currentIndex = 0;
        for(AdaptiveLearningQuizQuestionAnswer existingQuizQuestionAnswer : adaptiveLearningQuizQuestionAnswers) {
            IAdaptiveLearningQuizQuestionAnswerVO questionAnswerWithEdit = questionAnswersWithEdits.get(currentIndex);
            existingQuizQuestionAnswer.setQuizQuestionAnswerText(questionAnswerWithEdit.getQuizQuestionAnswerText());
            existingQuizQuestionAnswer.setModifyDate(DateTime.now());
            existingQuizQuestionAnswer.setCorrectAnswer(currentIndex == correctAnswerIndex);
            iAdaptiveLearningQuizQuestionAnswerRepository.save(existingQuizQuestionAnswer);
            currentIndex++;
        }

    }




}
