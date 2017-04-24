package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionAnswer;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionAnswerVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
public class AdaptiveLearningQuizQuestionAnswerVO implements IAdaptiveLearningQuizQuestionAnswerVO {


    private Long id;

    private String quizQuestionAnswerText;

    private ELearningMediaContent quizQuestionAnswerMultiMedia;

    public boolean correctAnswer = false;


    public AdaptiveLearningQuizQuestionAnswerVO() {

    }

    public AdaptiveLearningQuizQuestionAnswerVO(AdaptiveLearningQuizQuestionAnswer adaptiveLearningQuizQuestionAnswer) {
        Assert.notNull(adaptiveLearningQuizQuestionAnswer, "adaptiveLearningQuizQuestionAnswer cannot be null");
        this.id = adaptiveLearningQuizQuestionAnswer.getId();
        this.quizQuestionAnswerText = adaptiveLearningQuizQuestionAnswer.getQuizQuestionAnswerText();
        this.quizQuestionAnswerMultiMedia = adaptiveLearningQuizQuestionAnswer.getQuizQuestionAnswerMultiMedia();
        this.correctAnswer = adaptiveLearningQuizQuestionAnswer.isCorrectAnswer();
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public void setID(Long id) {
        this.id = id;
    }

    @Override
    public String getQuizQuestionAnswerText() {
        return quizQuestionAnswerText;
    }

    public void setQuizQuestionAnswerText(String quizQuestionAnswerText) {
        this.quizQuestionAnswerText = quizQuestionAnswerText;
    }

    @Override
    public ELearningMediaContent getQuizQuestionAnswerMultiMedia() {
        return quizQuestionAnswerMultiMedia;
    }

    public void setQuizQuestionAnswerMultiMedia(ELearningMediaContent quizQuestionAnswerMultiMedia) {
        this.quizQuestionAnswerMultiMedia = quizQuestionAnswerMultiMedia;
    }

    @Override
    public boolean isCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveLearningQuizQuestionAnswerVO that = (AdaptiveLearningQuizQuestionAnswerVO) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(correctAnswer, that.correctAnswer)
                .append(quizQuestionAnswerText, that.quizQuestionAnswerText)
                .append(quizQuestionAnswerMultiMedia, that.quizQuestionAnswerMultiMedia)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(quizQuestionAnswerText)
                .append(quizQuestionAnswerMultiMedia)
                .append(correctAnswer)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("quizQuestionAnswerText", quizQuestionAnswerText)
                .append("quizQuestionAnswerMultiMedia", quizQuestionAnswerMultiMedia)
                .append("correctAnswer", correctAnswer)
                .toString();
    }


}
