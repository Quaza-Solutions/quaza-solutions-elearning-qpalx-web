package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuiz;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestion;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizVO;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author manyce400
 */
public class AdaptiveLearningQuizWebVO implements IAdaptiveLearningQuizVO {



    private String quizTitle;

    private String quizDescription;

    private Long id;

    // Default max possible score on all quizzes to 100%
    private Double maxPossibleActivityScore = 100d;

    // Default min passing score to 70%
    private Double minimumPassingActivityScore = 70d;

    private Long timeToCompleteActivity;

    protected String activeFlag;

    // Using LinkedHashSet to maintain ordering
    private Set<IAdaptiveLearningQuizQuestionVO> adaptiveLearningQuizQuestionVOS = new LinkedHashSet<>();



    public AdaptiveLearningQuizWebVO() {

    }

    public AdaptiveLearningQuizWebVO(AdaptiveLearningQuiz adaptiveLearningQuiz) {
        Assert.notNull(adaptiveLearningQuiz, "adaptiveLearningQuiz cannot be null");
        this.quizTitle = adaptiveLearningQuiz.getQuizTitle();
        this.quizDescription = adaptiveLearningQuiz.getQuizDescription();
        this.id = adaptiveLearningQuiz.getId();
        this.maxPossibleActivityScore = adaptiveLearningQuiz.getMaxPossibleActivityScore();
        this.minimumPassingActivityScore = adaptiveLearningQuiz.getMinimumPassingActivityScore();
        this.timeToCompleteActivity = adaptiveLearningQuiz.getTimeToCompleteActivity();
        this.activeFlag = Boolean.toString(adaptiveLearningQuiz.isActive());
        addAllQuizQuestions(adaptiveLearningQuiz);
    }

    private void addAllQuizQuestions(AdaptiveLearningQuiz adaptiveLearningQuiz) {
        Set<AdaptiveLearningQuizQuestion> adaptiveLearningQuizQuestions = adaptiveLearningQuiz.getAdaptiveLearningQuizQuestions();
        for(AdaptiveLearningQuizQuestion adaptiveLearningQuizQuestion : adaptiveLearningQuizQuestions) {
            AdaptiveLearningQuizQuestionVO adaptiveLearningQuizQuestionVO = new AdaptiveLearningQuizQuestionVO(adaptiveLearningQuizQuestion);
            adaptiveLearningQuizQuestionVOS.add(adaptiveLearningQuizQuestionVO);
        }
    }

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
    public Long getID() {
        return id;
    }

    @Override
    public void setID(Long id) {
        if(this.id != null) {
            // Web thymeleaf layer attempts to rest this ID after during an edit operation so this will prevent.
            return;
        }

        this.id = id;
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

    public void replaceAdaptiveLearningQuizQuestionVO(IAdaptiveLearningQuizQuestionVO itemToReplaceWith) {
        Assert.notNull(itemToReplaceWith, "itemToReplaceWith cannot be null");

        // TODO this is inefficient, design better way to do this
        // create a new linked list to copy all questions to
        Set<IAdaptiveLearningQuizQuestionVO> modifiedQuestionsSet = new LinkedHashSet<>();

        // Because the actual properties of the Quiz Question could change, this replace will only use == to find instance to replace
        for(Iterator<IAdaptiveLearningQuizQuestionVO> iterator = adaptiveLearningQuizQuestionVOS.iterator(); iterator.hasNext();) {
            IAdaptiveLearningQuizQuestionVO iteratorItem = iterator.next();


            if(iteratorItem.getID().equals(itemToReplaceWith.getID())) {
                modifiedQuestionsSet.add(itemToReplaceWith);
            } else {
                modifiedQuestionsSet.add(iteratorItem);
            }
        }

        // Add new value
        adaptiveLearningQuizQuestionVOS.clear();
        adaptiveLearningQuizQuestionVOS.addAll(modifiedQuestionsSet);
    }

    public IAdaptiveLearningQuizQuestionVO getIAdaptiveLearningQuizQuestionVOByID(Long id) {
        Assert.notNull(id, "id cannot be null");

        for(IAdaptiveLearningQuizQuestionVO iAdaptiveLearningQuizQuestionVO : adaptiveLearningQuizQuestionVOS) {
            if(id.equals(iAdaptiveLearningQuizQuestionVO.getID())) {
                return iAdaptiveLearningQuizQuestionVO;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveLearningQuizWebVO that = (AdaptiveLearningQuizWebVO) o;

        return new EqualsBuilder()
                .append(quizTitle, that.quizTitle)
                .append(quizDescription, that.quizDescription)
                .append(id, that.id)
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
                .append(id)
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
                .append("id", id)
                .append("maxPossibleActivityScore", maxPossibleActivityScore)
                .append("minimumPassingActivityScore", minimumPassingActivityScore)
                .append("timeToCompleteActivity", timeToCompleteActivity)
                .append("activeFlag", activeFlag)
                .append("adaptiveLearningQuizQuestionVOS", adaptiveLearningQuizQuestionVOS)
                .toString();
    }


    public static void main(String[] args) {
        Set<IAdaptiveLearningQuizQuestionVO> testItems = new LinkedHashSet<>();
        AdaptiveLearningQuizQuestionVO one = new AdaptiveLearningQuizQuestionVO();
        one.setID(1L);

        AdaptiveLearningQuizQuestionVO two = new AdaptiveLearningQuizQuestionVO();
        two.setQuestionTitle("Instance to be replaced");
        two.setID(2L);

        AdaptiveLearningQuizQuestionVO three = new AdaptiveLearningQuizQuestionVO();
        three.setID(3L);

        AdaptiveLearningQuizQuestionVO four = new AdaptiveLearningQuizQuestionVO();
        four.setQuestionTitle("Replaced Instance");
        four.setID(2L);

        AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO = new AdaptiveLearningQuizWebVO();
        adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(one);
        adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(two);
        adaptiveLearningQuizWebVO.addAdaptiveLearningQuizQuestionVO(three);

        System.out.println("Before Replace Questions = " + adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs());

        adaptiveLearningQuizWebVO.replaceAdaptiveLearningQuizQuestionVO(four);

        System.out.println("\n\n");
        System.out.println("After Replace Questions = " + adaptiveLearningQuizWebVO.getIAdaptiveLearningQuizQuestionVOs());
    }
}
