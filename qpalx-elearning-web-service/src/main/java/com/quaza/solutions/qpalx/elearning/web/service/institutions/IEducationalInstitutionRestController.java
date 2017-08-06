package com.quaza.solutions.qpalx.elearning.web.service.institutions;

import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialLevel;

import java.util.List;

/**
 * @author manyce400
 */
public interface IEducationalInstitutionRestController {


    public List<StudentTutorialLevel> findAllAcademicLevelsForMunicipality(Long municipalityID);

    public List<StudentTutorialGrade> findAllStudentTutoralGradesForAcademicLevel(Long studentTutorialLevelID);

    public List<QPalXEducationalInstitution> findAllEducationalInstitutions(Long municipalityID, Long studentTutorialLevelID);


}
