package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import org.springframework.ui.Model;

/**
 * Defines an API for enriching and adding attribute details to a model specifically used by a QPalX web panel display.
 *
 * @author manyce400
 */
public interface IContentAdminTutorialGradePanelService {


    public void addDisplayPanelAttributes(Model model, Boolean addLessonsEnabled, Boolean addMicroLessonsEnabled, Boolean addMicroLessonsActivitiesEnabled, ELearningCourse eLearningCourse);

    public void addDisplayPanelAttributes(Model model, Boolean addLessonsEnabled, Boolean addMicroLessonsEnabled, Boolean addMicroLessonsActivitiesEnabled, QPalXELesson qPalXELesson);

    public void addDisplayPanelAttributes(Model model, Boolean addCoursesEnabled, Boolean addCourseActivitiesEnabled, String studentTutorialGradeID, String curriculumType);

}
