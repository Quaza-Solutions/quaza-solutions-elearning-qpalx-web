package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
public class AdaptiveQuizQuestionStudentResponseVO {


    private Integer quizQuestionModelID;

    private String userSelectedAnswerText;

    public AdaptiveQuizQuestionStudentResponseVO() {

    }

    public Integer getQuizQuestionModelID() {
        return quizQuestionModelID;
    }

    public void setQuizQuestionModelID(Integer quizQuestionModelID) {
        this.quizQuestionModelID = quizQuestionModelID;
    }

    public String getUserSelectedAnswerText() {
        return userSelectedAnswerText;
    }

    public void setUserSelectedAnswerText(String userSelectedAnswerText) {
        this.userSelectedAnswerText = userSelectedAnswerText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveQuizQuestionStudentResponseVO that = (AdaptiveQuizQuestionStudentResponseVO) o;

        return new EqualsBuilder()
                .append(quizQuestionModelID, that.quizQuestionModelID)
                .append(userSelectedAnswerText, that.userSelectedAnswerText)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(quizQuestionModelID)
                .append(userSelectedAnswerText)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("quizQuestionModelID", quizQuestionModelID)
                .append("userSelectedAnswerText", userSelectedAnswerText)
                .toString();
    }
}
