package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.math3.util.Precision;

/**
 * @author manyce400
 */
public class AdaptiveLearningQuizResultVO {


    private final int totalQuizQuestions;

    private int totalQuestionsCorrect;

    private String allQuizQuestionAnswerFeedBack = "";


    public AdaptiveLearningQuizResultVO(final int totalQuizQuestions) {
        this.totalQuizQuestions = totalQuizQuestions;
    }

    public int getTotalQuizQuestions() {
        return totalQuizQuestions;
    }

    public int getTotalQuestionsCorrect() {
        return totalQuestionsCorrect;
    }


    public int getTotalQuestionsIncorrect() {
        return totalQuizQuestions - totalQuestionsCorrect;
    }

    public String getAllQuizQuestionAnswerFeedBack() {
        return allQuizQuestionAnswerFeedBack;//HtmlUtils.htmlEscape(allQuizQuestionAnswerFeedBack);
    }

    public void appendQuestionFeedBack(String questionFeedBack) {
        StringBuffer allFeedBack = new StringBuffer(allQuizQuestionAnswerFeedBack);
        allQuizQuestionAnswerFeedBack = allFeedBack.append("<br/><br/>")
                .append(questionFeedBack)
                .toString();
    }

    public void incrementTotalQuestionsCorrect() {
        totalQuestionsCorrect++;
    }


    public double getQuizScorePercent() {
        if (totalQuestionsCorrect > 0 && totalQuizQuestions > 0) {
            double score = (totalQuestionsCorrect / totalQuizQuestions) * 100;
            return Precision.round(score, 0);
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveLearningQuizResultVO that = (AdaptiveLearningQuizResultVO) o;

        return new EqualsBuilder()
                .append(totalQuizQuestions, that.totalQuizQuestions)
                .append(totalQuestionsCorrect, that.totalQuestionsCorrect)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(totalQuizQuestions)
                .append(totalQuestionsCorrect)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("totalQuizQuestions", totalQuizQuestions)
                .append("totalQuestionsCorrect", totalQuestionsCorrect)
                .append("totalQuestionsIncorrect", getTotalQuestionsIncorrect())
                .append("allQuizQuestionAnswerFeedBack", allQuizQuestionAnswerFeedBack)
                .toString();
    }

}
