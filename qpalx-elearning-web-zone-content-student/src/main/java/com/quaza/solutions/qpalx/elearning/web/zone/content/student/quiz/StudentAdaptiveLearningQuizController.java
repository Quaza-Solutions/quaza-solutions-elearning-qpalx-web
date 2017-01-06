package com.quaza.solutions.qpalx.elearning.web.zone.content.student.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionAnswer;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.scorable.IAdaptiveLearningExperienceService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.TutorialCalendarPanelE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.user.quiz.IStudentQuizQuestionService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizResultVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveQuizQuestionStudentResponseVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"SelectedQPalXELesson", "SelectedTutorialCalendar", "LaunchedAdaptiveLearningQuiz", "LaunchedAdaptiveLearningQuizQuestions", "LaunchedAdaptiveLearningQuizQuestionScores"})
public class StudentAdaptiveLearningQuizController {


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizService2")
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.StudentQuizQuestionService")
    private IStudentQuizQuestionService iStudentQuizQuestionService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningExperienceService")
    private IAdaptiveLearningExperienceService iAdaptiveLearningExperienceService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizStatisticsService")
    private IAdaptiveLearningQuizStatisticsService iAdaptiveLearningQuizStatisticsService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.DefaultRedirectStrategyExecutor")
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentAdaptiveLearningQuizController.class);


    @RequestMapping(value = "/launch-adaptive-quiz", method = RequestMethod.GET)
    public String launchAdaptiveLearningQuiz(final Model model, ModelMap modelMap, @RequestParam("quizID") String quizID, @RequestParam("eLessonID") String eLessonID, @RequestParam("tutorialLevelID") String tutorialLevelID) {
        LOGGER.info("Launching AdaptiveLearningQuiz with ID: {}", quizID);

        Long id = NumberUtils.toLong(quizID);
        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(id);
        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuiz.toString(), adaptiveLearningQuiz);

        // Build model for all Quizzes questions
        Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap = iStudentQuizQuestionService.getAdaptiveQuizQuestionsModel(adaptiveLearningQuiz);
        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestions.toString(), questionModelMap);

        // Track the ELessonID and TutorialLevel Calendar as part of this Quiz Session so we could go back to viewing all micro lessons for the lesson we are currently dealing with.
        modelMap.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), eLessonID);
        modelMap.addAttribute(TutorialCalendarPanelE.SelectedTutorialCalendar.toString(), tutorialLevelID);

        // Get the Introductory video for the Lesson that this Quiz is in.
        return ContentRootE.Student_Adaptive_Learning_Quiz.getContentRootPagePath("launch-quiz");
    }

    @RequestMapping(value = "/access-quiz-question", method = RequestMethod.GET)
    public String accessAdaptiveLearningQuizQuestion(final Model model, ModelMap modelMap,  @RequestParam("questionID") String questionID,
                                                     @ModelAttribute("LaunchedAdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
                                                     @ModelAttribute("LaunchedAdaptiveLearningQuizQuestions") Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap) {
        LOGGER.info("Accessing QPalX Adaptive Quiz Question with Quiz Model ID: {}", questionID);

        Integer id = NumberUtils.toInt(questionID);
        AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion = questionModelMap.get(id);
        model.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestion.toString(), adaptiveLearningQuizQuestion);

        // Create Question Student score value object
        AdaptiveQuizQuestionStudentResponseVO adaptiveQuizQuestionStudentResponseVO = new AdaptiveQuizQuestionStudentResponseVO();
        adaptiveQuizQuestionStudentResponseVO.setQuizQuestionModelID(id);
        model.addAttribute(AdaptiveLearningQuizAttributeE.AdaptiveQuizQuestionScoreVO.toString(), adaptiveQuizQuestionStudentResponseVO);

        // will next question be end of entire quiz
        int nextQuestionIndex = id;
        if(nextQuestionIndex == questionModelMap.size()) {
            model.addAttribute(AdaptiveLearningQuizAttributeE.ShowQuizCalculationTrigger.toString(), Boolean.TRUE);
        } else {
            model.addAttribute(AdaptiveLearningQuizAttributeE.ShowQuizCalculationTrigger.toString(), Boolean.FALSE);
        }

        Set<AdaptiveLearningQuizQuestionAnswer> quizQuestionAnswers = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();
        model.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizQuestionAnswers.toString(), quizQuestionAnswers);
        return ContentRootE.Student_Adaptive_Learning_Quiz.getContentRootPagePath("quiz-question-page");
    }

    @RequestMapping(value = "/submit-quiz-question-answer", method = RequestMethod.POST)
    public void processQuizQuestionAnswerSubmit(final Model model, ModelMap modelMap,
                                                @ModelAttribute("LaunchedAdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
                                                @ModelAttribute("AdaptiveQuizQuestionStudentResponseVO") AdaptiveQuizQuestionStudentResponseVO adaptiveQuizQuestionStudentResponseVO,
                                                @ModelAttribute("LaunchedAdaptiveLearningQuizQuestions") Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap,
                                                HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Processing Student user's response answer to Quiz: adaptiveQuizQuestionStudentResponseVO: {}", adaptiveQuizQuestionStudentResponseVO);

        // record and track Student Question answer score and send to next question
        iStudentQuizQuestionService.addAdaptiveQuizQuestionScoreVO(modelMap, adaptiveQuizQuestionStudentResponseVO);

        // Check to see if this Quiz has more questions
        int totalQuestionsAvailable = questionModelMap.size();

        if (adaptiveQuizQuestionStudentResponseVO.getQuizQuestionModelID() < totalQuestionsAvailable) {
            Integer nextQuestionModelID = adaptiveQuizQuestionStudentResponseVO.getQuizQuestionModelID().intValue() +1;
            String targetURL = "/access-quiz-question?questionID="+nextQuestionModelID;
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        } else {
            String targetURL = "/show-quiz-scores";
            iRedirectStrategyExecutor.sendRedirect(request, response, targetURL);
        }
    }

    @RequestMapping(value = "/show-quiz-scores", method = RequestMethod.GET)
    public String calculateAndShowQuizQuestionScores(final Model model, ModelMap modelMap,
                                                   @ModelAttribute("LaunchedAdaptiveLearningQuiz") AdaptiveLearningQuiz adaptiveLearningQuiz,
                                                   @ModelAttribute("LaunchedAdaptiveLearningQuizQuestionScores") List<AdaptiveQuizQuestionStudentResponseVO> adaptiveQuizQuestionStudentResponseVO,
                                                   @ModelAttribute("LaunchedAdaptiveLearningQuizQuestions") Map<Integer, AdaptiveLearningQuizQuestion> questionModelMap,
                                                   HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("Calculating entire quiz results...");

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        // capture and caluclate the entire quiz results for student
        AdaptiveLearningQuizResultVO adaptiveLearningQuizResultVO = iStudentQuizQuestionService.calculateAdaptiveQuizResults(adaptiveQuizQuestionStudentResponseVO, questionModelMap);
        model.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuizResultVO.toString(), adaptiveLearningQuizResultVO);
        LOGGER.info("Results of Quiz Calculation: {}", adaptiveLearningQuizResultVO);

        // Save this as an AdaptiveLearningExperience and record Adaptive Quiz progress statistics.
        iAdaptiveLearningExperienceService.buildAndSaveAdaptiveLearningExperience(optionalUser.get(), QPalXTutorialContentTypeE.Quiz, adaptiveLearningQuizResultVO.getQuizScorePercent(), adaptiveLearningQuiz.getId());
        iAdaptiveLearningQuizStatisticsService.recordAdaptiveLearningQuizProgress(adaptiveLearningQuiz.getId(), optionalUser.get());
        return ContentRootE.Student_Adaptive_Learning_Quiz.getContentRootPagePath("quiz-results-page");
    }

}
