package com.quaza.solutions.qpalx.elearning.web.zone.content.student.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionAnswer;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizReviewAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.user.quiz.IStudentQuizQuestionService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveQuizQuestionStudentResponseVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Controller that handles all functionality for reviewing A quiz's answers and feedback after Student completes quiz.
 *
 * @author manyce400 `
 */
@Controller
@SessionAttributes(value = {"LaunchedAdaptiveLearningQuiz", "LaunchedAdaptiveLearningQuizQuestions", "LaunchedAdaptiveLearningQuizQuestionScores"})
public class StudentAdaptiveLearningQuizReviewController {




    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizService2")
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.StudentQuizQuestionService")
    private IStudentQuizQuestionService iStudentQuizQuestionService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentAdaptiveLearningQuizReviewController.class);


    @RequestMapping(value = "/review-quiz-question-answers", method = RequestMethod.POST)
    public String reviewAdaptiveLearningQuizQuestion(final Model model, ModelMap modelMap, @RequestParam("quizQuestionModelID") String quizQuestionModelID,
                                                     @ModelAttribute("LaunchedAdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
                                                     @ModelAttribute("LaunchedAdaptiveLearningQuizQuestions") Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap) {
        LOGGER.info("Reviewing QPalX Adaptive Quiz Question with Quiz Model ID: {}", quizQuestionModelID);

        // Lookup the Quiz Question Model
        int currentQuizQuestionModelID = NumberUtils.toInt(quizQuestionModelID);
        int nextQuestionToReviewID = currentQuizQuestionModelID +  1;
        AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion = questionModelMap.get(currentQuizQuestionModelID);
        model.addAttribute(AdaptiveLearningQuizReviewAttributeE.NextQuestionToReviewModelID.toString(), nextQuestionToReviewID);
        model.addAttribute(AdaptiveLearningQuizReviewAttributeE.ReviewAdaptiveLearningQuizQuestion.toString(), adaptiveLearningQuizQuestion);

        // display all Quiz Answers
        for(AdaptiveLearningQuizQuestionAnswer adaptiveLearningQuizQuestionAnswer : adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers()) {
            System.out.println("adaptiveLearningQuizQuestionAnswer.getQuizQuestionAnswerText() = " + adaptiveLearningQuizQuestionAnswer.getQuizQuestionAnswerText() + " IsCorrect: " + adaptiveLearningQuizQuestionAnswer.isCorrectAnswer());
        }

        // Find Students response to this question
        AdaptiveQuizQuestionStudentResponseVO studentResponseVO = iStudentQuizQuestionService.findAdaptiveQuizQuestionStudentResponse(modelMap, currentQuizQuestionModelID);
        System.out.println("\nstudentResponseVO = " + studentResponseVO);

        model.addAttribute(AdaptiveLearningQuizAttributeE.QuizQuestionStudentAnswerResponse.toString(), studentResponseVO);

        if(currentQuizQuestionModelID == questionModelMap.size()) {
            model.addAttribute(AdaptiveLearningQuizReviewAttributeE.ContinueQuizQuestionsReview.toString(), Boolean.FALSE);
        } else {
            model.addAttribute(AdaptiveLearningQuizReviewAttributeE.ContinueQuizQuestionsReview.toString(), Boolean.TRUE);
        }

        return ContentRootE.Student_Adaptive_Learning_Quiz.getContentRootPagePath("quiz-question-review-page");
    }


    @RequestMapping(value = "/exit-adaptive-quiz", method = RequestMethod.GET)
    public void reviewAdaptiveLearningQuizQuestion(final SessionStatus status, HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam("eLessonID") String eLessonID, @RequestParam("tutorialLevelID") String tutorialLevelID) {
        LOGGER.info("Exiting Adaptive Quiz for Student, flushing Quiz session...");
        String targetURL = "/view-micro-lessons?eLessonID=" + eLessonID + "&tutorialLevelID=" + tutorialLevelID;
        iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
    }

}
