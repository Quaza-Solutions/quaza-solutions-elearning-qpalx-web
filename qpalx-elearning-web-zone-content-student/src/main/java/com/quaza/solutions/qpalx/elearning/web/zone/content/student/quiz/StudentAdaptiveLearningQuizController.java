package com.quaza.solutions.qpalx.elearning.web.zone.content.student.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.quiz.IAdaptiveLearningQuizService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author manyce400
 */
@Controller
public class StudentAdaptiveLearningQuizController {


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLearningQuizService2")
    private IAdaptiveLearningQuizService iAdaptiveLearningQuizService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentAdaptiveLearningQuizController.class);


    @RequestMapping(value = "/launch-adaptive-quiz", method = RequestMethod.GET)
    public String launchAdaptiveLearningQuiz(final Model model, @RequestParam("quizID") String quizID) {
        LOGGER.info("Launching AdaptiveLearningQuiz with ID: {}", quizID);

        Long id = NumberUtils.toLong(quizID);
        AdaptiveLearningQuiz adaptiveLearningQuiz = iAdaptiveLearningQuizService.findByID(id);
        model.addAttribute(AdaptiveLearningQuizAttributeE.LaunchedAdaptiveLearningQuiz.toString(), adaptiveLearningQuiz);
        return ContentRootE.Student_Adaptive_Learning_Quiz.getContentRootPagePath("launch-quiz");
    }
}
