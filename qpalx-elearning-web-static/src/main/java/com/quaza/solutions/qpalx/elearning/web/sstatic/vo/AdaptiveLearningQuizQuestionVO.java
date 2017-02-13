package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionAnswerVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
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

    private String quizQuestionTypeString;

    private AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE;

    private ELearningMediaContent quizQuestionMultiMedia;

    private IHierarchicalLMSContent iHierarchicalLMSContent;


    private Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS = new LinkedHashSet<>();

    public AdaptiveLearningQuizQuestionVO() {
    }

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

    public String getQuizQuestionTypeString() {
        return quizQuestionTypeString;
    }

    public void setQuizQuestionTypeString(String quizQuestionTypeString) {
        this.quizQuestionTypeString = quizQuestionTypeString;
    }

    @Override
    public AdaptiveLearningQuizQuestionTypeE getAdaptiveLearningQuizQuestionTypeE() {
        return adaptiveLearningQuizQuestionTypeE;
    }

    public void setAdaptiveLearningQuizQuestionTypeE(AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE) {
        this.adaptiveLearningQuizQuestionTypeE = adaptiveLearningQuizQuestionTypeE;
    }

    @Override
    public ELearningMediaContent getQuizQuestionMultiMedia() {
        return quizQuestionMultiMedia;
    }

    public void setQuizQuestionAnswerMultiMedia(ELearningMediaContent quizQuestionMultiMedia) {
        this.quizQuestionMultiMedia = quizQuestionMultiMedia;
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
    public Set<MediaContentTypeE> getMediaContentTypes() {
        return ImmutableSet.of(MediaContentTypeE.jpeg, MediaContentTypeE.png);
    }

    @Override
    public Set<QPalXTutorialContentTypeE> getQPalXTutorialContentTypes() {
        return ImmutableSet.of(QPalXTutorialContentTypeE.Quiz_Question);
    }

    @Override
    public QPalXTutorialContentTypeE getSelectedQPalXTutorialContentTypeE() {
        return QPalXTutorialContentTypeE.Quiz_Question;
    }

    @Override
    public IHierarchicalLMSContent getIHierarchicalLMSContent() {
        return iHierarchicalLMSContent;
    }

    public void setIHierarchicalLMSContent(IHierarchicalLMSContent iHierarchicalLMSContent) {
        this.iHierarchicalLMSContent = iHierarchicalLMSContent;
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
                .append(quizQuestionMultiMedia, that.quizQuestionMultiMedia)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(questionTitle)
                .append(questionFeedBack)
                .append(adaptiveLearningQuizQuestionTypeE)
                .append(quizQuestionMultiMedia)
                .toHashCode();
    }

    @Override
    public String toString() {
        String qizQuestionAnswerModel = super.toString();
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("questionTitle", questionTitle)
                .append("questionFeedBack", questionFeedBack)
                .append("adaptiveLearningQuizQuestionTypeE", adaptiveLearningQuizQuestionTypeE)
                .append("quizQuestionMultiMedia", quizQuestionMultiMedia)
                .append("iHierarchicalLMSContent", iHierarchicalLMSContent)
                .append("qizQuestionAnswerModel", qizQuestionAnswerModel)
                .toString();
    }
}
