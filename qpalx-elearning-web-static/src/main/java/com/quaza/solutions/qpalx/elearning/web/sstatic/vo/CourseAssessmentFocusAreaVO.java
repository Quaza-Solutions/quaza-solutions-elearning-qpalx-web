package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.quaza.solutions.qpalx.elearning.domain.lms.assessment.ICourseAssessmentFocusAreaVO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
public class CourseAssessmentFocusAreaVO extends AdaptiveLearningQuizWebVO implements ICourseAssessmentFocusAreaVO {


    private Long eLearningCourseID;

    private Long focusAreaUniqueID;

    private Long courseAssessmentFocusAreaID;

    public static final String CLASS_ATTRIBUTE_IDENTIFIER = "CourseAssessmentFocusAreaVO";

    @Override
    public Long getELearningCourseID() {
        return eLearningCourseID;
    }

    public void setELearningCourseID(Long eLearningCourseID) {
        this.eLearningCourseID = eLearningCourseID;
    }

    @Override
    public Long getCourseAssessmentFocusAreaID() {
        return courseAssessmentFocusAreaID;
    }

    public void setCourseAssessmentFocusAreaID(Long courseAssessmentFocusAreaID) {
        this.courseAssessmentFocusAreaID = courseAssessmentFocusAreaID;
    }

    public Long getFocusAreaUniqueID() {
        return focusAreaUniqueID;
    }

    public void setFocusAreaUniqueID(Long focusAreaUniqueID) {
        this.focusAreaUniqueID = focusAreaUniqueID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CourseAssessmentFocusAreaVO that = (CourseAssessmentFocusAreaVO) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(courseAssessmentFocusAreaID, that.courseAssessmentFocusAreaID)
                .append(eLearningCourseID, that.eLearningCourseID)
                .append(focusAreaUniqueID, that.focusAreaUniqueID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(eLearningCourseID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("courseAssessmentFocusAreaID", courseAssessmentFocusAreaID)
                .append("eLearningCourseID", eLearningCourseID)
                .append("focusAreaUniqueID", focusAreaUniqueID)
                .append("quizTitle", quizTitle)
                .append("quizDescription", quizDescription)
                .append("id", id)
                .append("maxPossibleActivityScore", maxPossibleActivityScore)
                .append("minimumPassingActivityScore", minimumPassingActivityScore)
                .append("timeToCompleteActivity", timeToCompleteActivity)
                .append("activeFlag", activeFlag)
                .toString();
    }
}
