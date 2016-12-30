package com.quaza.solutions.qpalx.elearning.web.zone.content.student.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionAnswer;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.user.quiz.IStudentQuizQuestionService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"LaunchedAdaptiveLearningQuiz", "LaunchedAdaptiveLearningQuizQuestions"})
public class StudentAdaptiveLearningQuizController {


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizService2")
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.StudentQuizQuestionService")
    private IStudentQuizQuestionService iStudentQuizQuestionService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentAdaptiveLearningQuizController.class);


    @RequestMapping(value = "/launch-adaptive-quiz", method = RequestMethod.GET)
    public String launchAdaptiveLearningQuiz(final Model model, ModelMap modelMap, @RequestParam("quizID") String quizID) {
        LOGGER.info("Launching AdaptiveLearningQuiz with ID: {}", quizID);

        Long id = NumberUtils.toLong(quizID);
        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(id);
        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuiz.toString(), adaptiveLearningQuiz);

        // Build model for all Quizzes questions
        Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap = iStudentQuizQuestionService.getAdaptiveQuizQuestionsModel(adaptiveLearningQuiz);
        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestions.toString(), questionModelMap);
        System.out.println("questionModelMap.size() = " + questionModelMap.size());

        // Get the Introductory video for the Lesson that this Quiz is in.
        //adaptiveLearningQuiz.getQPalXEMicroLesson()
        return ContentRootE.Student_Adaptive_Learning_Quiz.getContentRootPagePath("launch-quiz");
    }

    @RequestMapping(value = "/access-quiz-question", method = RequestMethod.GET)
    public String accessAdaptiveLearningQuizQuestion(final Model model, ModelMap modelMap,  @RequestParam("questionID") String questionID,
                                                     @ModelAttribute("LaunchedAdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
                                                     @ModelAttribute("LaunchedAdaptiveLearningQuizQuestions") Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap) {
        LOGGER.info("Accessing QPalX Adaptive Quiz Question for Quiz Title: {}", adaptiveLearningQuiz.getQuizTitle());

        Integer id = NumberUtils.toInt(questionID);
        AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion = questionModelMap.get(id);
        model.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestion.toString(), adaptiveLearningQuizQuestion);

        Set<AdaptiveLearningQuizQuestionAnswer> quizQuestionAnswers = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();
        model.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestionAnswers.toString(), quizQuestionAnswers);
        return ContentRootE.Student_Adaptive_Learning_Quiz.getContentRootPagePath("quiz-question-page");
    }
}
