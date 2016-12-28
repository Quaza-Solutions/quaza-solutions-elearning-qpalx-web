package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionAnswerVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author manyce400
 */
public class AdaptiveLearningQuizQuestionVO extends AdaptiveQuizQuestionAnswerModel implements IAdaptiveLearningQuizQuestionVO {



    private String questionTitle;

    private String questionFeedBack;

    private AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE;

    private ELearningMediaContent quizQuestionAnswerMultiMedia;


    private Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS = new LinkedHashSet<>();


    @Override
    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    @Override
    public String getQuestionFeedBack() {
        return questionFeedBack;
    }

    public void setQuestionFeedBack(String questionFeedBack) {
        this.questionFeedBack = questionFeedBack;
    }

    @Override
    public AdaptiveLearningQuizQuestionTypeE getAdaptiveLearningQuizQuestionTypeE() {
        return adaptiveLearningQuizQuestionTypeE;
    }

    public void setAdaptiveLearningQuizQuestionTypeE(AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE) {
        this.adaptiveLearningQuizQuestionTypeE = adaptiveLearningQuizQuestionTypeE;
    }

    @Override
    public ELearningMediaContent getQuizQuestionAnswerMultiMedia() {
        return quizQuestionAnswerMultiMedia;
    }

    public void setQuizQuestionAnswerMultiMedia(ELearningMediaContent quizQuestionAnswerMultiMedia) {
        this.quizQuestionAnswerMultiMedia = quizQuestionAnswerMultiMedia;
    }

    @Override
    public Set<IAdaptiveLearningQuizQuestionAnswerVO> getIAdaptiveLearningQuizQuestionAnswerVOs() {
        return ImmutableSet.copyOf(iAdaptiveLearningQuizQuestionAnswerVOS);
    }

    public void buildAndAddQuestionAnswerModel() {
        Set<IAdaptiveLearningQuizQuestionAnswerVO> quizQuestionAnswers = getQuizQuestionAnswers();
        iAdaptiveLearningQuizQuestionAnswerVOS.addAll(quizQuestionAnswers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveLearningQuizQuestionVO that = (AdaptiveLearningQuizQuestionVO) o;

        return new EqualsBuilder()
                .append(questionTitle, that.questionTitle)
                .append(questionFeedBack, that.questionFeedBack)
                .append(adaptiveLearningQuizQuestionTypeE, that.adaptiveLearningQuizQuestionTypeE)
                .append(quizQuestionAnswerMultiMedia, that.quizQuestionAnswerMultiMedia)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(questionTitle)
                .append(questionFeedBack)
                .append(adaptiveLearningQuizQuestionTypeE)
                .append(quizQuestionAnswerMultiMedia)
                .toHashCode();
    }

    @Override
    public String toString() {
        String questionModel = super.toString();
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("questionTitle", questionTitle)
                .append("questionFeedBack", questionFeedBack)
                .append("adaptiveLearningQuizQuestionTypeE", adaptiveLearningQuizQuestionTypeE)
                .append("quizQuestionAnswerMultiMedia", quizQuestionAnswerMultiMedia)
                .append("AdaptiveQuizQuestionAnswerModel", questionModel)
                .toString();
    }
}
