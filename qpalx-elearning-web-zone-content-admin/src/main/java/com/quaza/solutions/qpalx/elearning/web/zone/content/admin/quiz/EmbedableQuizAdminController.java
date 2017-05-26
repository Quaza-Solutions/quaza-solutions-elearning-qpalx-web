package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.quiz.maker.ClassicQuizMaker;
import com.quaza.solutions.qpalx.elearning.web.service.quiz.maker.ClassicQuizMakerSessionTracker;
import com.quaza.solutions.qpalx.elearning.web.service.quiz.maker.IClassicQuizMaker;
import com.quaza.solutions.qpalx.elearning.web.service.quiz.maker.IClassicQuizMakerSessionTracker;
import com.quaza.solutions.qpalx.elearning.web.service.utils.DefaultRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.domain.EmbedableQuizSetting;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(
        value = {
                IHierarchicalLMSContent.CLASS_ATTRIBUTE_IDENTIFIER, AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE, EmbedableQuizSetting.QUIZ_PARENT_LMS_CONTENT_NAME, EmbedableQuizSetting.QUIZ_PARENT_LMS_CONTENT_TYPE
        }
)
public class EmbedableQuizAdminController {




    @Autowired
    @Qualifier(ClassicQuizMaker.BEAN_NAME)
    private IClassicQuizMaker iClassicQuizMaker;

    @Autowired
    @Qualifier(ClassicQuizMakerSessionTracker.BEAN_NAME)
    IClassicQuizMakerSessionTracker iClassicQuizMakerSessionTracker;

    @Autowired
    @Qualifier(DefaultRedirectStrategyExecutor.BEAN_NAME)
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EmbedableQuizAdminController.class);



    @RequestMapping(value = "/launch-embedded-content-quiz-mode", method = {RequestMethod.GET, RequestMethod.POST})
    public String launchEmbeddedAdaptiveLearningQuiz(ModelMap modelMap,
                                                     @ModelAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE) AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
                                                     @ModelAttribute(IHierarchicalLMSContent.CLASS_ATTRIBUTE_IDENTIFIER) IHierarchicalLMSContent adaptiveQuizParentContent) {
        LOGGER.info("Starting embedded AdaptiveLearning Quiz: {} for parent content: {}", adaptiveLearningQuizWebVO, adaptiveQuizParentContent.getHierarchicalLMSContentName());
        iClassicQuizMakerSessionTracker.startAdaptiveQuizCreation(modelMap, adaptiveQuizParentContent, adaptiveLearningQuizWebVO);

        // Save the basic AdaptiveLearningQuiz
        iClassicQuizMaker.makeBasicQuiz(adaptiveLearningQuizWebVO);
        return ContentRootE.Content_Admin_Embeded_Quiz.getContentRootPagePath("embedded-quiz-home");
    }


    @RequestMapping(value = "/launch-embedded-quiz-question-by-type", method = {RequestMethod.GET, RequestMethod.POST})
    public String launchQuizQuestionMode(Model model, @RequestParam("quizQuestionType") String quizQuestionType) {
        LOGGER.info("Launching quiz question view for type {}", quizQuestionType);

        // Look up quiz question type
        AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE = AdaptiveLearningQuizQuestionTypeE.getByName(quizQuestionType);

        AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO = new AdaptiveLearningQuizQuestionVO();
        adaptiveLearningQuizQuestionVO.setAdaptiveLearningQuizQuestionTypeE(adaptiveLearningQuizQuestionTypeE);

        model.addAttribute(AdaptiveLearningQuizQuestionVO.CLASS_ATTRIBUTE_IDENTIFIER, adaptiveLearningQuizQuestionVO);
        return ContentRootE.Content_Admin_Embeded_Quiz.getContentRootPagePath("embedded-question-by-type");
    }


    @RequestMapping(value = "/save-embedded-quiz-question", method = RequestMethod.POST)
    public String saveEmbeddedAdaptiveLearningQuizQuestion(Model model,
                                                           ModelMap modelMap,
                                                           @ModelAttribute(AdaptiveLearningQuizQuestionVO.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO,
                                                           @ModelAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE) AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
                                                           @ModelAttribute(IHierarchicalLMSContent.CLASS_ATTRIBUTE_IDENTIFIER) IHierarchicalLMSContent adaptiveQuizParentContent) {
        LOGGER.info("Saving changes to quiz question: {}", adaptiveLearningQuizQuestionVO);

        // Build and add all question and provided answers to Quiz
        adaptiveLearningQuizQuestionVO.buildAndAddQuestionAnswerModel();

        // Get question attributes and save the question with answers directly to DB
        int questionOrder = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs().size() + 1;
        adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(adaptiveQuizParentContent);
        adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
        //iClassicQuizMaker.modifyQuizWithQuestion(adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, Optional.empty(), questionOrder);

        // Get the list of all questions and answers as a map to return for display.
        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);
        return ContentRootE.Content_Admin_Embeded_Quiz.getContentRootPagePath("embedded-quiz-home");
    }













    @RequestMapping(value = "/embed-quiz-question-type", method = RequestMethod.GET)
    public String embedQuizQuestionType(Model model,
                                        HttpServletRequest request,
                                        @RequestParam("quizQuestionType") String quizQuestionType) {
        LOGGER.info("Embedding panel for selected quiz question type: {} in display", quizQuestionType);

        // IF this is a result of a redirect add any web operations errrors to model
        iRedirectStrategyExecutor.addWebOperationRedirectErrorsToModel(model, request);

        // Look up quiz question type
        AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE = AdaptiveLearningQuizQuestionTypeE.getByName(quizQuestionType);

        LOGGER.info("Constructed AdaptiveLearningQuizQuestionVO object...");
        AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO = new AdaptiveLearningQuizQuestionVO();
        adaptiveLearningQuizQuestionVO.setAdaptiveLearningQuizQuestionTypeE(adaptiveLearningQuizQuestionTypeE);

        model.addAttribute(AdaptiveLearningQuizQuestionVO.CLASS_ATTRIBUTE_IDENTIFIER, adaptiveLearningQuizQuestionVO);

        return ContentRootE.Content_Admin_Embeded_Quiz.getContentRootPagePath("embedded-question-by-type");







    }

//    @RequestMapping(value = "/add-embeded-question-by-type-to-model", method = RequestMethod.POST)
//    public String addEmbededQuestionByTypeToInternalModel(Model model,
//                                                          @ModelAttribute(IHierarchicalLMSContent.CLASS_ATTRIBUTE_IDENTIFIER) IHierarchicalLMSContent iHierarchicalLMSContent,
//                                                          @ModelAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE) AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO,
//                                                          @ModelAttribute(AdaptiveLearningQuizQuestionVO.CLASS_ATTRIBUTE_IDENTIFIER) AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO) {
//        LOGGER.info("Adding Quiz question details to model for adaptiveLearningQuizQuestionVO: {}", adaptiveLearningQuizQuestionVO);
//
//
//        // Build and add all question and provided answers to Quiz
//        adaptiveLearningQuizQuestionVO.buildAndAddQuestionAnswerModel();
//
//        // Validate the question details, IF anything is missing redirect back to Quiz Question page
//        Optional<String> optionalValidationMessage = adaptiveLearningQuizQuestionVO.getValidationMessage();
//        if(optionalValidationMessage.isPresent()) {
//            LOGGER.warn("Found Validation Issue: {}, redirecting back to Question entry...", optionalValidationMessage.get());
//            model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveLearningQuizQuestionVO.toString(), adaptiveLearningQuizQuestionVO);
//            model.addAttribute(WebOperationErrorAttributesE.Invalid_FORM_Submission.toString(), optionalValidationMessage.get());
//            //iRedirectStrategyExecutor.sendRedirectWithError(targetURL, errorMessage, WebOperationErrorAttributesE.Invalid_FORM_Submission, request, response);
//            return ContentRootE.Content_Admin_Embeded_Quiz.getContentRootPagePath("quiz-question-by-type");
//        }
//
//        if (adaptiveLearningQuizQuestionVO.getID() == null) {
//            LOGGER.info("Saving new QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
//            int questionOrder = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs().size() + 1;
//            adaptiveLearningQuizQuestionVO.setIHierarchicalLMSContent(iHierarchicalLMSContent);
//            adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
//            iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, questionOrder);
//        } else {
//            LOGGER.info("Updating previously saved QuizQuestion: {}", adaptiveLearningQuizQuestionVO);
//            iAdaptiveLearningQuizService.saveAdaptiveLearningQuizDetails(qPalXEMicroLesson, adaptiveLearningQuizWebVO, adaptiveLearningQuizQuestionVO, 0);
//            adaptiveLearningQuizWebVO.replaceAdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestionVO);
//        }
//
//        // Get the list of all questions and answers as a map to return for display.
//        Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOSet = adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs();
//        model.addAttribute(AdaptiveLearningQuizAttributeE.CurrentAdaptiveLearningQuizQuestionVOs.toString(), adaptiveLearningQuizQuestionVOSet);
//
//        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
//    }

}
