package com.quaza.solutions.qpalx.elearning.web.service.quiz.editor;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IEditableAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author manyce400
 */
public interface IClassicQuizEditor {

    public void updateQuizWithEdits(AdaptiveLearningQuiz adaptiveLearningQuiz, AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO);

    public void updateQuizQuestionWithEdits(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion, IEditableAdaptiveLearningQuizQuestionVO iEditableAdaptiveLearningQuizQuestionVO);

    public void updateQuizQuestionWithEdits(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion, IEditableAdaptiveLearningQuizQuestionVO iEditableAdaptiveLearningQuizQuestionVO, MultipartFile multipartFile);

    public IEditableAdaptiveLearningQuizQuestionVO getEditableNewQuizQuestion(AdaptiveLearningQuiz adaptiveLearningQuiz, String quizQuestionType);


}
