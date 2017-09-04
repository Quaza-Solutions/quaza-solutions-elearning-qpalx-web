package com.quaza.solutions.qpalx.elearning.web.zone.content.student.progress;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.statistics.IAdaptiveLessonStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.DefaultQPalxUserService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IQPalxUserService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController that provides services for allow recording and tracking of MicroLessons completion progress for a
 * QPalXUser
 *
 * @author manyce400
 */
@RestController
public class MicroLessonsProgressTrackerRestController {




    @Autowired
    @Qualifier(DefaultQPalxUserService.BEAN_NAME)
    private IQPalxUserService iqPalxUserService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXEMicroLessonService")
    private IQPalXEMicroLessonService iqPalXEMicroLessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.AdaptiveLessonStatisticsService")
    private IAdaptiveLessonStatisticsService iAdaptiveLessonStatisticsService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MicroLessonsProgressTrackerRestController.class);

    @RequestMapping(value = "/micro-lesson-progress-tracker", method = RequestMethod.GET)
    public void recordMicroLessonStatisticsEvent(@RequestParam("microLessonID") String microLessonID, @RequestParam("uniqueQPalxUserKey") String uniqueQPalxUserKey) {
        LOGGER.info("Attempting to record MicroLesson with ID: {} progress for QPalXUser with email: {}", microLessonID, uniqueQPalxUserKey);

        if(microLessonID != null && uniqueQPalxUserKey != null) {
            Long id = NumberUtils.toLong(microLessonID);

            // Locate the user and the QPalXEMicroLesson
            QPalXUser qPalXUser = iqPalxUserService.findQPalXUserByEmail(uniqueQPalxUserKey);
            QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(id);

            // Record this as a micro lesson statistics event
            LOGGER.info("Recording adaptive learning statistics event....");
            iAdaptiveLessonStatisticsService.recordAdaptiveLessonStatistics(qPalXEMicroLesson, qPalXUser);
        }
    }

}
