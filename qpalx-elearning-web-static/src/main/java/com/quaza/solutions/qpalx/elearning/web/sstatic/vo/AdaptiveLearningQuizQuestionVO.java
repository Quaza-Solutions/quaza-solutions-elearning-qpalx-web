package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.*;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400
 */
public class AdaptiveLearningQuizQuestionVO extends AdaptiveQuizQuestionAnswerModel implements IAdaptiveLearningQuizQuestionVO {



    private String questionTitle;

    private String questionFeedBack;

    private String quizQuestionTypeString;

    private Long id;

    private AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE;

    private ELearningMediaContent quizQuestionMultiMedia;

    private IHierarchicalLMSContent iHierarchicalLMSContent;


    private Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS = new LinkedHashSet<>();

    public AdaptiveLearningQuizQuestionVO() {

    }

    public AdaptiveLearningQuizQuestionVO(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion) {
        Assert.notNull(adaptiveLearningQuizQuestion, "adaptiveLearningQuizQuestion cannot be null");
        this.questionTitle = adaptiveLearningQuizQuestion.getQuestionTitle();
        this.questionFeedBack = adaptiveLearningQuizQuestion.getQuestionFeedBack();
        this.quizQuestionTypeString = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionTypeE().toString();
        this.id = adaptiveLearningQuizQuestion.getId();
        this.adaptiveLearningQuizQuestionTypeE = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionTypeE();
        this.quizQuestionMultiMedia = adaptiveLearningQuizQuestion.getQuizQuestionMultiMedia();
        addAllQuestionAnswers(adaptiveLearningQuizQuestion);
    }

    private void addAllQuestionAnswers(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion) {
        Set<AdaptiveLearningQuizQuestionAnswer> adaptiveLearningQuizQuestionAnswers = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();
        for(AdaptiveLearningQuizQuestionAnswer adaptiveLearningQuizQuestionAnswer : adaptiveLearningQuizQuestionAnswers) {
            AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO = new AdaptiveLearningQuizQuestionAnswerVO(adaptiveLearningQuizQuestionAnswer);
            iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO);
        }
        buildQuestionAnswerModel(iAdaptiveLearningQuizQuestionAnswerVOS);
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
    public Long getID() {
        return id;
    }

    @Override
    public void setID(Long id) {
        this.id = id;
    }

    public void setAdaptiveLearningQuizQuestionId(Long id) {
        this.id = id;
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
        if (adaptiveLearningQuizQuestionTypeE == AdaptiveLearningQuizQuestionTypeE.Image) {
            return ImmutableSet.of(MediaContentTypeE.jpeg, MediaContentTypeE.png);
        } else if (adaptiveLearningQuizQuestionTypeE == AdaptiveLearningQuizQuestionTypeE.Video) {
            return ImmutableSet.of(MediaContentTypeE.mp4);
        }

        // Only Image and Video question types will support media content.
        return ImmutableSet.of();
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

    public Optional<String> getValidationMessage() {
        // Verify non empty questionTitle and questionFeedBack
        if(questionTitle == null || questionTitle.length() == 0) {
            return Optional.of("Question Title is Required.");
        }

        if(questionFeedBack == null || questionFeedBack.length() == 0) {
            return Optional.of("Question FeedBack is Required.");
        }

        if(correctAnswer1 == null || correctAnswer1.length() == 0) {
            return Optional.of("Select the Correct Answer to Quiz Question.");
        }

        if(iAdaptiveLearningQuizQuestionAnswerVOS.size() == 0) {
            return Optional.of("Quiz Question Ansers are required.");
        }

        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveLearningQuizQuestionVO that = (AdaptiveLearningQuizQuestionVO) o;

        return new EqualsBuilder()
                .append(questionTitle, that.questionTitle)
                .append(questionFeedBack, that.questionFeedBack)
                .append(id, that.id)
                .append(adaptiveLearningQuizQuestionTypeE, that.adaptiveLearningQuizQuestionTypeE)
                .append(quizQuestionMultiMedia, that.quizQuestionMultiMedia)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(questionTitle)
                .append(questionFeedBack)
                .append(id)
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
                .append("id", id)
                .append("adaptiveLearningQuizQuestionTypeE", adaptiveLearningQuizQuestionTypeE)
                .append("quizQuestionMultiMedia", quizQuestionMultiMedia)
                .append("iHierarchicalLMSContent", iHierarchicalLMSContent)
                .append("qizQuestionAnswerModel", qizQuestionAnswerModel)
                .toString();
    }
}
