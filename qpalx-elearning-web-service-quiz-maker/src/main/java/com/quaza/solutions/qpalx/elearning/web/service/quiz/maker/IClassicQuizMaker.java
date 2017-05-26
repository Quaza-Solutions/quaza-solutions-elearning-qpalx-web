package com.quaza.solutions.qpalx.elearning.web.service.quiz.maker;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author manyce400
 */
public interface IClassicQuizMaker {

    public void makeBasicQuiz(IAdaptiveLearningQuizVO iAdaptiveLearningQuizVO);

    public ClassicQuizMakerError modifyQuizWithQuestion(IAdaptiveLearningQuizVO iAdaptiveLearningQuizVO, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO, Optional<MultipartFile> optionalMultipartFile, Integer questionOrder);

}
