package com.quaza.solutions.qpalx.elearning.web.service.quiz.maker;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.AdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.ELearningStaticContentService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent.IELearningStaticContentService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ClassicQuizMaker.BEAN_NAME)
public class ClassicQuizMaker implements IClassicQuizMaker {


    @Autowired
    @Qualifier(AdaptiveLearningQuizService.BEAN_NAME)
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    @Qualifier(ELearningStaticContentService.BEAN_NAME)
    private IELearningStaticContentService ieLearningStaticContentService;

    @Autowired
    @Qualifier(ClassicQuizQuestionMaker.BEAN_NAME)
    private IClassicQuizQuestionMaker iClassicQuizQuestionMaker;

    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.quiz.maker.ClassicQuizMaker";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClassicQuizMaker.class);


    public void startAdaptiveQuizCreation(IAdaptiveLearningQuizVO iAdaptiveLearningQuizVO, ModelMap modelMap) {

    }

    @Override
    public void makeBasicQuiz(IAdaptiveLearningQuizVO iAdaptiveLearningQuizVO) {
        Assert.notNull(iAdaptiveLearningQuizVO, "iAdaptiveLearningQuizVO cannot be null");
        LOGGER.debug("Making basic quiz from iAdaptiveLearningQuizVO: {}", iAdaptiveLearningQuizVO);

        AdaptiveLearningQuiz adaptiveLearningQuiz = AdaptiveLearningQuiz.builder()
                .quizTitle(iAdaptiveLearningQuizVO.getQuizTitle())
                .quizDescription(iAdaptiveLearningQuizVO.getQuizDescription())
                .maxPossibleActivityScore(iAdaptiveLearningQuizVO.getMaxPossibleActivityScore())
                .minimumPassingActivityScore(iAdaptiveLearningQuizVO.getMinimumPassingActivityScore())
                .entryDate(DateTime.now())
                .active(true)
                .build();

        iAdaptiveLearningQuizService.saveQuiz(adaptiveLearningQuiz);

        // synchronize with DB persisted id
        iAdaptiveLearningQuizVO.setID(adaptiveLearningQuiz.getId());
    }

    @Transactional
    @Override
    public ClassicQuizMakerError modifyQuizWithQuestion(IAdaptiveLearningQuizVO iAdaptiveLearningQuizVO, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO, Optional<MultipartFile> optionalMultipartFile, Integer questionOrder) {
        Assert.notNull(iAdaptiveLearningQuizVO, "iAdaptiveLearningQuizVO cannot be null");
        Assert.notNull(iAdaptiveLearningQuizVO.getID(), "quiz does not have a persisted id");
        Assert.notNull(iAdaptiveLearningQuizQuestionVO, "iAdaptiveLearningQuizQuestionVO cannot be null");
        Assert.notNull(questionOrder, "questionOrder cannot be null");

        LOGGER.debug("Modifying Quiz with id: {} adding new question: {}", iAdaptiveLearningQuizQuestionVO.getID(), iAdaptiveLearningQuizQuestionVO.getQuestionTitle());

        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(iAdaptiveLearningQuizVO.getID());

        // IF quiz question contains multimedia file, upload and set on object to persist
        ClassicQuizMakerError classicQuizMakerError = uploadQuestionMultipartIFAvailable(optionalMultipartFile, iAdaptiveLearningQuizQuestionVO);
        if(classicQuizMakerError.hasErrors()) {
            return classicQuizMakerError;
        }

        return iClassicQuizQuestionMaker.persistQuizQuestionDetails(adaptiveLearningQuiz, iAdaptiveLearningQuizQuestionVO, questionOrder);
    }

    private ClassicQuizMakerError uploadQuestionMultipartIFAvailable(Optional<MultipartFile> optionalMultipartFile, IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO) {
        if(optionalMultipartFile.isPresent()) {
            LOGGER.debug("Quiz question contains multipart file: {} attempting to upload...", optionalMultipartFile);
            ELearningMediaContent eLearningMediaContent = ieLearningStaticContentService.uploadELearningMediaContent(optionalMultipartFile.get(), iAdaptiveLearningQuizQuestionVO);

            if (eLearningMediaContent == null) {
                LOGGER.warn("Failed to upload ELearningMediaContent for question");
                String errorMessage = "Failed to upload file: Check the contents of the file";
                return ClassicQuizMakerError.of(errorMessage);
            } else if (eLearningMediaContent == ELearningMediaContent.NOT_SUPPORTED_MEDIA_CONTENT) {
                LOGGER.warn("Failed to upload ELearningMediaContent for question, found unsupported media type");
                String errorMessage = "Uploaded file is not supported: Only Files of type(JPEG, PNG) supported";
                return ClassicQuizMakerError.of(errorMessage);
            }

            // set on vo object to be persisted
            iAdaptiveLearningQuizQuestionVO.setQuizQuestionAnswerMultiMedia(eLearningMediaContent);
        }

        return ClassicQuizMakerError.NO_ERRORS;
    }

}
