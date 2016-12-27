package com.quaza.solutions.qpalx.elearning.web.service.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXELessonService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXELessonWebVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXEMicroLessonVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QuestionBankVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.sstatic.CurriculumHierarchyService")
public class CurriculumHierarchyService implements ICurriculumHierarchyService {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultELearningCourseService")
    private IELearningCourseService ieLearningCourseService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXELessonService")
    private IQPalXELessonService iqPalXELessonService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXEMicroLessonService")
    private IQPalXEMicroLessonService iqPalXEMicroLessonService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CurriculumHierarchyService.class);


    @Override
    public void buildHierarchyForQPalXELessonWebVO(QPalXELessonWebVO qPalXELessonWebVO) {
        Assert.notNull(qPalXELessonWebVO, "qPalXELessonWebVO cannot be null");

        LOGGER.info("Building hierarchical information for new QPalx Lesson web object....");

        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(qPalXELessonWebVO.getELearningCourseID());
        QPalXELesson qPalXELesson = new QPalXELesson();
        qPalXELesson.seteLearningCourse(eLearningCourse);
        qPalXELesson.setLessonName(qPalXELessonWebVO.getLessonName());
        qPalXELesson.setLessonDescription(qPalXELesson.getLessonDescription());
        qPalXELessonWebVO.setIHierarchicalLMSContent(qPalXELesson);
    }

    @Override
    public void buildHierarchyForQPalXEMicroLessonVO(QPalXEMicroLessonVO qPalXEMicroLessonVO) {
        Assert.notNull(qPalXEMicroLessonVO, "qPalXELessonWebVO cannot be null");

        LOGGER.info("Building hierarchical information for new QPalxMicro Lesson web object....");

        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(qPalXEMicroLessonVO.getQPalXELessonID());
        QPalXEMicroLesson qPalXEMicroLesson = new QPalXEMicroLesson();
        qPalXEMicroLesson.setQPalXELesson(qPalXELesson);
        qPalXEMicroLesson.setMicroLessonName(qPalXEMicroLessonVO.getMicroLessonName());
        qPalXEMicroLesson.setMicroLessonDescription(qPalXEMicroLessonVO.getMicroLessonDescription());
        qPalXEMicroLessonVO.setiHierarchicalLMSContent(qPalXELesson);
    }

    @Override
    public void buildHierarchyForQuestionBankVO(QuestionBankVO questionBankVO) {
        Assert.notNull(questionBankVO, "qPalXELessonWebVO cannot be null");
    }


}
