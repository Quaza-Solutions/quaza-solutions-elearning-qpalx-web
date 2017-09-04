package com.quaza.solutions.qpalx.elearning.web.zone.content.student.progress;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.scorable.QuestionBankItem;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.scorable.IQuestionBankService;
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
 * @author manyce400
 */
@RestController
public class QuestionBankProgressTrackerRestController {



    @Autowired
    @Qualifier(DefaultQPalxUserService.BEAN_NAME)
    private IQPalxUserService iqPalxUserService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QuestionBankService")
    private IQuestionBankService iQuestionBankService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StatisticsExecutorService")
    private ListeningExecutorService listeningExecutorService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QuestionBankProgressTrackerRestController.class);


    @RequestMapping(value = "/question-bank-progress-tracker", method = RequestMethod.GET)
    public void recordQuestionBankStatisticsEvent(@RequestParam("questionBankItemID") String questionBankItemID, @RequestParam("uniqueQPalxUserKey") String uniqueQPalxUserKey) {
        LOGGER.info("Attempting to record QuestionBankItem with ID: {} progress for QPalXUser with email: {}", questionBankItemID, uniqueQPalxUserKey);

        Runnable statisticsRecordTask = () -> {
            if (questionBankItemID != null && uniqueQPalxUserKey != null) {
                Long id = NumberUtils.toLong(questionBankItemID);

                // Get the qPalxUser for which metrics will be tracked as well as the questionBankItem
                QPalXUser qPalXUser = iqPalxUserService.findQPalXUserByEmail(uniqueQPalxUserKey);
                QuestionBankItem questionBankItem = iQuestionBankService.findByID(id);
                iQuestionBankService.recordAdaptiveLessonStatistics(questionBankItem, qPalXUser);
            }
        };

        listeningExecutorService.submit(statisticsRecordTask);
    }

}
