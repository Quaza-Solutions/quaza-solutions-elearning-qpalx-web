package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizVO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author manyce400
 */
public class AdaptiveLearningQuizWebVO implements IAdaptiveLearningQuizVO {



    private String quizTitle;

    private String quizDescription;

    private Double maxPossibleActivityScore = 100d;

    private Double minimumPassingActivityScore;

    private Long timeToCompleteActivity;

    protected String activeFlag;

    // Using LinkedHashSet to maintain ordering
    private Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOS = new LinkedHashSet<>();


    @Override
    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    @Override
    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    @Override
    public Double getMaxPossibleActivityScore() {
        return maxPossibleActivityScore;
    }

    public void setMaxPossibleActivityScore(Double maxPossibleActivityScore) {
        this.maxPossibleActivityScore = maxPossibleActivityScore;
    }

    @Override
    public Double getMinimumPassingActivityScore() {
        return minimumPassingActivityScore;
    }

    public void setMinimumPassingActivityScore(Double minimumPassingActivityScore) {
        this.minimumPassingActivityScore = minimumPassingActivityScore;
    }

    @Override
    public Long getTimeToCompleteActivity() {
        return timeToCompleteActivity;
    }

    public void setTimeToCompleteActivity(Long timeToCompleteActivity) {
        this.timeToCompleteActivity = timeToCompleteActivity;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public boolean isActive() {
        return Boolean.valueOf(activeFlag);
    }

    @Override
    public Set<IAdaptiveLearningQuizQuestionVO> getIAdaptiveLearningQuizQuestionVOs() {
        return ImmutableSet.copyOf(adaptiveLearningQuizQuestionVOS);
    }

    public void addAdaptiveLearningQuizQuestionVO(IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO) {
        Assert.notNull(iAdaptiveLearningQuizQuestionVO, "iAdaptiveLearningQuizQuestionVO cannot be null");
        adaptiveLearningQuizQuestionVOS.add(iAdaptiveLearningQuizQuestionVO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveLearningQuizWebVO that = (AdaptiveLearningQuizWebVO) o;

        return new EqualsBuilder()
                .append(quizTitle, that.quizTitle)
                .append(quizDescription, that.quizDescription)
                .append(maxPossibleActivityScore, that.maxPossibleActivityScore)
                .append(minimumPassingActivityScore, that.minimumPassingActivityScore)
                .append(timeToCompleteActivity, that.timeToCompleteActivity)
                .append(activeFlag, that.activeFlag)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(quizTitle)
                .append(quizDescription)
                .append(maxPossibleActivityScore)
                .append(minimumPassingActivityScore)
                .append(timeToCompleteActivity)
                .append(activeFlag)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("quizTitle", quizTitle)
                .append("quizDescription", quizDescription)
                .append("maxPossibleActivityScore", maxPossibleActivityScore)
                .append("minimumPassingActivityScore", minimumPassingActivityScore)
                .append("timeToCompleteActivity", timeToCompleteActivity)
                .append("activeFlag", activeFlag)
                .append("adaptiveLearningQuizQuestionVOS", adaptiveLearningQuizQuestionVOS)
                .toString();
    }
}
