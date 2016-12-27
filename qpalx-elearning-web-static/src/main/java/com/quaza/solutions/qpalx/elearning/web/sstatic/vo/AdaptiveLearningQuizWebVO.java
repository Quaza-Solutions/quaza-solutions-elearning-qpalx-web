package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizVO;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

/**
 * @author manyce400
 */
public class AdaptiveLearningQuizWebVO implements IAdaptiveLearningQuizVO {



    private String quizTitle;

    private String quizDescription;

    private Double maxPossibleActivityScore;

    private Double minimumPassingActivityScore;

    private Long timeToCompleteActivity;

    protected String activeFlag;


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
        return null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("quizTitle", quizTitle)
                .append("quizDescription", quizDescription)
                .append("maxPossibleActivityScore", maxPossibleActivityScore)
                .append("minimumPassingActivityScore", minimumPassingActivityScore)
                .append("timeToCompleteActivity", timeToCompleteActivity)
                .append("activeFlag", activeFlag)
                .toString();
    }
}
