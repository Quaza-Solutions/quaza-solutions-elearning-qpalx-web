package com.quaza.solutions.qpalx.elearning.web.service.admin.quiz;

import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.CourseAssessmentFocusAreaVO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(EmbededAdaptiveLearningQuizService.BEAN_NAME)
public class EmbededAdaptiveLearningQuizService implements IEmbededAdaptiveLearningQuizService {




    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.admin.quiz.EmbededAdaptiveLearningQuizService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EmbededAdaptiveLearningQuizService.class);


    @Override
    public void saveAndUpdateEmbededAdaptiveQuiz(AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO) {
        Assert.notNull(adaptiveLearningQuizWebVO, "adaptiveLearningQuizWebVO");
        LOGGER.debug("Saving and updating adaptiveLearningQuizWebVO: {}", adaptiveLearningQuizWebVO);

        if(adaptiveLearningQuizWebVO instanceof CourseAssessmentFocusAreaVO) {
            // Handle quiz affiliated with a specific Course assesment.
            CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO = (CourseAssessmentFocusAreaVO) adaptiveLearningQuizWebVO;
            LOGGER.info("Handling quiz associated with a Course assessment in Course with ID: {}", courseAssessmentFocusAreaVO.getELearningCourseID());
        }
    }


}
