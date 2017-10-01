package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.IELearningCourseVO;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by manyce400 on 8/7/16.
 */
public class ELearningCourseWebVO implements IELearningCourseVO {


    public Long id;

    public String courseName;

    public String courseDescription;

    private Long eLearningCurriculumID;

    private Long educationalInstitutionID;

    public ELearningCourseWebVO() {

    }

    public ELearningCourseWebVO(ELearningCourse eLearningCourse) {
        this.id = eLearningCourse.getId();
        this.courseName = eLearningCourse.getCourseName();
        this.courseDescription = eLearningCourse.getCourseDescription();
        this.eLearningCurriculumID = eLearningCourse.geteLearningCurriculum().getId();

        if (eLearningCourse.getqPalXEducationalInstitution() != null) {
            this.educationalInstitutionID = eLearningCourse.getqPalXEducationalInstitution().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    @Override
    public Long getELearningCurriculumID() {
        return eLearningCurriculumID;
    }

    public void seteLearningCurriculumID(Long eLearningCurriculumID) {
        this.eLearningCurriculumID = eLearningCurriculumID;
    }

    @Override
    public Long getEducationalInstitutionID() {
        return educationalInstitutionID;
    }

    public void setEducationalInstitutionID(Long educationalInstitutionID) {
        this.educationalInstitutionID = educationalInstitutionID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("courseName", courseName)
                .append("courseDescription", courseDescription)
                .append("eLearningCurriculumID", eLearningCurriculumID)
                .append("educationalInstitutionID", educationalInstitutionID)
                .toString();
    }
}
