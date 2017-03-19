package com.quaza.solutions.qpalx.elearning.web.service.user.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.scorable.AdaptiveLearningExperience;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.geographical.DefaultGeographicalDateTimeFormatter;
import com.quaza.solutions.qpalx.elearning.service.geographical.IGeographicalDateTimeFormatter;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.scorable.AdaptiveLearningExperienceService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.scorable.IAdaptiveLearningExperienceService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@RestController
public class StudentQuizSummaryResultsRestController {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(DefaultGeographicalDateTimeFormatter.SPRING_BEAN_NAME)
    private IGeographicalDateTimeFormatter iGeographicalDateTimeFormatter;

    @Autowired
    @Qualifier(AdaptiveLearningExperienceService.SPRING_BEAN_NAME)
    private IAdaptiveLearningExperienceService iAdaptiveLearningExperienceService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentQuizSummaryResultsRestController.class);


    @CrossOrigin
    @RequestMapping(value = "/StudentQuizPerformance", method = RequestMethod.GET)
    public List<StudentQuizPerformance> getStudentQuizPerformanceSnapshot(@RequestParam(value = "QuizID") String quizID) {
        LOGGER.info("Finding and retrieving Student Quiz performance snapshot for Quiz with ID: {}", quizID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        if (optionalUser.isPresent()) {
            LOGGER.info("Quiz performance snapshot will be retrieved for student: {}", optionalUser.get().getEmail());

            List<StudentQuizPerformance> results = new ArrayList<>();

            Long id = NumberUtils.toLong(quizID);
            List<AdaptiveLearningExperience> quizLearningExperiences = iAdaptiveLearningExperienceService.findAllWithScorableActivityID(id, optionalUser.get());
            System.out.println("quizLearningExperiences = " + quizLearningExperiences);

            for (AdaptiveLearningExperience adaptiveLearningExperience : quizLearningExperiences) {
                String javascriptSafeDateTime = iGeographicalDateTimeFormatter.getJavaScriptSafeDisplayDateTimeWithTimeZone(adaptiveLearningExperience.getLearningExperienceStartDate(), optionalUser.get().getQPalXMunicipality());
                String userFriendlyDateTimeDisplay = iGeographicalDateTimeFormatter.getUserFriendlyDateTime(adaptiveLearningExperience.getLearningExperienceStartDate(), optionalUser.get().getQPalXMunicipality());
                StudentQuizPerformance studentQuizPerformance = new StudentQuizPerformance(adaptiveLearningExperience.getProficiencyScore(), javascriptSafeDateTime, userFriendlyDateTimeDisplay);
                results.add(studentQuizPerformance);
            }

            return results;
        } else {
            StudentQuizPerformance studentQuizPerformance1 = new StudentQuizPerformance(50, "2017,0,1,09,00", "1st Jan, 2017 9:00");
            StudentQuizPerformance studentQuizPerformance2 = new StudentQuizPerformance(70, "2017,0,1,12,30", "1st Jan, 2017 12:30");
            StudentQuizPerformance studentQuizPerformance3 = new StudentQuizPerformance(30, "2017,0,3,9,30", "3rd Jan, 2017 9:30");
            StudentQuizPerformance studentQuizPerformance4 = new StudentQuizPerformance(90, "2017,0,4,13,03", "4th Jan, 2017 13:03");
            StudentQuizPerformance studentQuizPerformance5 = new StudentQuizPerformance(80, "2017,0,5,14,04", "5th Jan, 2017 14:04");
            StudentQuizPerformance studentQuizPerformance6 = new StudentQuizPerformance(40, "2017,0,6,17,30", "6th Jan, 2017 17:30");
            List<StudentQuizPerformance> results = new ArrayList<>();
            results.add(studentQuizPerformance1);
            results.add(studentQuizPerformance2);
            results.add(studentQuizPerformance3);
            results.add(studentQuizPerformance4);
            results.add(studentQuizPerformance5);
            results.add(studentQuizPerformance6);
            return results;
        }
    }
}
