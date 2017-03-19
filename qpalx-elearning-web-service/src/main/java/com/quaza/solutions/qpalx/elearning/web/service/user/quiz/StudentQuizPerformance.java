package com.quaza.solutions.qpalx.elearning.web.service.user.quiz;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author manyce400
 */
public class StudentQuizPerformance {


    private double quizScore;

    private String javaScriptSafeDateTime;

    private String userFriendlyDateTimeDisplay;

    public StudentQuizPerformance(double quizScore, String javaScriptSafeDateTime, String userFriendlyDateTimeDisplay) {
        this.quizScore = quizScore;
        this.javaScriptSafeDateTime = javaScriptSafeDateTime;
        this.userFriendlyDateTimeDisplay = userFriendlyDateTimeDisplay;
    }

    public double getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(double quizScore) {
        this.quizScore = quizScore;
    }

    public String getJavaScriptSafeDateTime() {
        return javaScriptSafeDateTime;
    }

    public void setJavaScriptSafeDateTime(String javaScriptSafeDateTime) {
        this.javaScriptSafeDateTime = javaScriptSafeDateTime;
    }

    public String getUserFriendlyDateTimeDisplay() {
        return userFriendlyDateTimeDisplay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StudentQuizPerformance that = (StudentQuizPerformance) o;

        return new EqualsBuilder()
                .append(quizScore, that.quizScore)
                .append(javaScriptSafeDateTime, that.javaScriptSafeDateTime)
                .append(userFriendlyDateTimeDisplay, that.userFriendlyDateTimeDisplay)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(quizScore)
                .append(javaScriptSafeDateTime)
                .append(userFriendlyDateTimeDisplay)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("quizScore", quizScore)
                .append("javaScriptSafeDateTime", javaScriptSafeDateTime)
                .append("userFriendlyDateTimeDisplay", userFriendlyDateTimeDisplay)
                .toString();
    }
}
