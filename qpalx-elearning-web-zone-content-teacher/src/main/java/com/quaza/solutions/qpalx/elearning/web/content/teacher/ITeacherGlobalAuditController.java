package com.quaza.solutions.qpalx.elearning.web.content.teacher;

/**
 * @author manyce400
 */
public interface ITeacherGlobalAuditController {

    // Displays all this Teacher's associated Student's performance in a given curriculum
    public String findAffiliatedStudentsCurriculumPerfomance(Long eLearningCurriculum);


}
