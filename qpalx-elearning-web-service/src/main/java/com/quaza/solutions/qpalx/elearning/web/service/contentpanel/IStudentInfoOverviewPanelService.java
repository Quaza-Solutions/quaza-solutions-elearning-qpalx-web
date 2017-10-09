package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface IStudentInfoOverviewPanelService {


    public void addStudentInfoOverviewWithCurriculum(Model model, Long curriculumID);

    public void addStudentInfoOverviewWithCurriculum(Model model, ELearningCurriculum eLearningCurriculum);

    public void addStudentInfoOverviewWithCourse(Model model, Long courseID);

    public void addStudentInfoOverviewWithCourse(Model model, ELearningCourse eLearningCourse);

    public void addStudentInfoOverviewWithLesson(Model model, Long lessonID);

    public void addStudentInfoOverviewWithLesson(Model model, QPalXELesson qPalXELesson);

    public void addStudentInfoOverviewWithMicroLesson(Model model, QPalXEMicroLesson qPalXEMicroLesson);

}
