package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface IAcademicLevelAdminPanelService {


    public void addAdministratorAcademicGradeLevels(Model model, CurriculumType curriculumType, StudentTutorialGrade studentTutorialGrade);

    public StudentTutorialGrade findBaseAdminAssignedStudentTutorialGrade();

}
