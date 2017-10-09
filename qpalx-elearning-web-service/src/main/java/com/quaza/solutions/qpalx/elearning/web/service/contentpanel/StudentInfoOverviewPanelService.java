package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCourseService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXELessonService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(StudentInfoOverviewPanelService.BEAN_NAME)
public class StudentInfoOverviewPanelService implements IStudentInfoOverviewPanelService {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledELearningCurriculumService")
    private IELearningCurriculumService iELearningCurriculumService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultELearningCourseService")
    private IELearningCourseService ieLearningCourseService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXELessonService")
    private IQPalXELessonService iqPalXELessonService;


    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.StudentInfoOverviewPanelService";


    @Override
    public void addStudentInfoOverviewWithCurriculum(Model model, Long curriculumID) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(curriculumID, "curriculumID cannot be null");
        ELearningCurriculum eLearningCurriculum = iELearningCurriculumService.findByELearningCurriculumID(curriculumID);
        addStudentInfoOverviewWithCurriculum(model, eLearningCurriculum);
    }

    @Override
    public void addStudentInfoOverviewWithCurriculum(Model model, ELearningCurriculum eLearningCurriculum) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(eLearningCurriculum, "eLearningCurriculum cannot be null");
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCurriculum.toString(), Boolean.TRUE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCourse.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayLesson.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCurriculum.toString(), eLearningCurriculum);
    }

    @Override
    public void addStudentInfoOverviewWithCourse(Model model, Long courseID) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(courseID, "courseID cannot be null");
        ELearningCourse eLearningCourse = ieLearningCourseService.findByCourseID(courseID);
        addStudentInfoOverviewWithCourse(model, eLearningCourse);
    }

    @Override
    public void addStudentInfoOverviewWithCourse(Model model, ELearningCourse eLearningCourse) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(eLearningCourse, "eLearningCourse cannot be null");
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCourse.toString(), Boolean.TRUE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCurriculum.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayLesson.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);
    }

    @Override
    public void addStudentInfoOverviewWithLesson(Model model, Long lessonID) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(lessonID, "lessonID cannot be null");
        QPalXELesson qPalXELesson = iqPalXELessonService.findQPalXELessonByID(lessonID);
        addStudentInfoOverviewWithLesson(model, qPalXELesson);
    }

    @Override
    public void addStudentInfoOverviewWithLesson(Model model, QPalXELesson qPalXELesson) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(qPalXELesson, "qPalXELesson cannot be null");
        model.addAttribute(CurriculumDisplayAttributeE.DisplayLesson.toString(), Boolean.TRUE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCourse.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCurriculum.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);
    }

    @Override
    public void addStudentInfoOverviewWithMicroLesson(Model model, QPalXEMicroLesson qPalXEMicroLesson) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(qPalXEMicroLesson, "qPalXEMicroLesson cannot be null");
        model.addAttribute(CurriculumDisplayAttributeE.DisplayMicroLesson.toString(), Boolean.TRUE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCourse.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.DisplayCurriculum.toString(), Boolean.FALSE.toString());
        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXMicroLesson.toString(), qPalXEMicroLesson);
    }
}
