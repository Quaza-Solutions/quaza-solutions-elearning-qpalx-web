package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.*;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.AbstractILMSMediaContentVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author manyce400
 */
public class EditableAdaptiveLearningQuizQuestionVO extends AbstractILMSMediaContentVO implements IEditableAdaptiveLearningQuizQuestionVO {


    private Long id;

    private String questionTitle;

    private String questionFeedBack;

    private String quizQuestionTypeString;

    private AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE;

    private ELearningMediaContent quizQuestionMultiMedia;

    private IHierarchicalLMSContent iHierarchicalLMSContent;

    private Integer correctAnswerIndex;

    private List<AdaptiveLearningQuizQuestionAnswerVO> quizQuestionAnswers = new LinkedList<>();


    public EditableAdaptiveLearningQuizQuestionVO() {

    }

    public EditableAdaptiveLearningQuizQuestionVO(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion) {
        Assert.notNull(adaptiveLearningQuizQuestion, "adaptiveLearningQuizQuestion cannot be null");
        this.id = id;
        this.questionTitle = adaptiveLearningQuizQuestion.getQuestionTitle();
        this.questionFeedBack = adaptiveLearningQuizQuestion.getQuestionFeedBack();
        this.quizQuestionTypeString = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionTypeE().toString();
        this.adaptiveLearningQuizQuestionTypeE = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionTypeE();
        this.iHierarchicalLMSContent = adaptiveLearningQuizQuestion.getAdaptiveLearningQuiz();
        addAllCurrentQuestionAnswers(adaptiveLearningQuizQuestion);
    }

    private void addAllCurrentQuestionAnswers(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion) {
        Set<AdaptiveLearningQuizQuestionAnswer> adaptiveLearningQuizQuestionAnswers = adaptiveLearningQuizQuestion.getAdaptiveLearningQuizQuestionAnswers();

        int answerIndex = 0;
        for(AdaptiveLearningQuizQuestionAnswer adaptiveLearningQuizQuestionAnswer : adaptiveLearningQuizQuestionAnswers) {
            AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO = new AdaptiveLearningQuizQuestionAnswerVO(adaptiveLearningQuizQuestionAnswer);

            if(adaptiveLearningQuizQuestionAnswer.isCorrectAnswer()) {
                correctAnswerIndex = answerIndex;
            }

            quizQuestionAnswers.add(adaptiveLearningQuizQuestionAnswerVO);
            answerIndex++;
        }
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
    public Set<MediaContentTypeE> getMediaContentTypes() {
        // Only Image and Video quizzes will sumpport MediaContentType
        if (adaptiveLearningQuizQuestionTypeE == AdaptiveLearningQuizQuestionTypeE.Image) {
            return MediaContentTypeE.getImageMediaTypes();
        } else if (adaptiveLearningQuizQuestionTypeE == AdaptiveLearningQuizQuestionTypeE.Video) {
            return ImmutableSet.of(MediaContentTypeE.mp4);
        }
        return ImmutableSet.of();
    }


    public String getQuizQuestionTypeString() {
        return quizQuestionTypeString;
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

    @Override
    public void setQuizQuestionAnswerMultiMedia(ELearningMediaContent quizQuestionMultiMedia) {
        this.quizQuestionMultiMedia = quizQuestionMultiMedia;
    }

    @Override
    public Set<QPalXTutorialContentTypeE> getQPalXTutorialContentTypes() {
        return ImmutableSet.of(QPalXTutorialContentTypeE.Quiz_Question);
    }

    public Integer getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(Integer correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public List<AdaptiveLearningQuizQuestionAnswerVO> getQuizQuestionAnswers() {
        return quizQuestionAnswers;
    }

    public void setQuizQuestionAnswers(List<AdaptiveLearningQuizQuestionAnswerVO> quizQuestionAnswers) {
        this.quizQuestionAnswers = quizQuestionAnswers;
    }

    @Override
    public List<IAdaptiveLearningQuizQuestionAnswerVO> getQuestionAnswersWithEdits() {
        return ImmutableList.copyOf(quizQuestionAnswers);
    }

    @Override
    public IHierarchicalLMSContent getIHierarchicalLMSContent() {
        return iHierarchicalLMSContent;
    }

    @Override
    public boolean isQuizEditValid() {
        return !StringUtils.isEmpty(questionTitle) &&
                StringUtils.isEmpty(questionFeedBack) &&
                correctAnswerIndex != null &&
                quizQuestionAnswers.size() == 4;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("activeFlag", activeFlag)
                .append("qPalXTutorialContentType", qPalXTutorialContentType)
                .append("eLearningMediaContent", eLearningMediaContent)
                .append("id", id)
                .append("questionTitle", questionTitle)
                .append("questionFeedBack", questionFeedBack)
                .append("quizQuestionTypeString", quizQuestionTypeString)
                .append("adaptiveLearningQuizQuestionTypeE", adaptiveLearningQuizQuestionTypeE)
                .append("quizQuestionMultiMedia", quizQuestionMultiMedia)
                .append("iHierarchicalLMSContent", iHierarchicalLMSContent)
                .append("correctAnswerIndex", correctAnswerIndex)
                .append("quizQuestionAnswers", quizQuestionAnswers)
                .toString();
    }

}
