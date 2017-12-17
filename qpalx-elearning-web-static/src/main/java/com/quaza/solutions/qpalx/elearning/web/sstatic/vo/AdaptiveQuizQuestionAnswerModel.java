package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.AdaptiveLearningQuizQuestionTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionAnswerVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.AbstractILMSMediaContentVO;
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
public abstract class AdaptiveQuizQuestionAnswerModel extends AbstractILMSMediaContentVO {



    protected String option1;

    protected String option2;

    protected String option3;

    protected String option4;

    protected String correctAnswer1;


    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCorrectAnswer1() {
        return correctAnswer1;
    }

    public void setCorrectAnswer1(String correctAnswer1) {
        this.correctAnswer1 = correctAnswer1;
    }

    public void buildQuestionAnswerModel(Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS) {
        Assert.notNull(iAdaptiveLearningQuizQuestionAnswerVOS, "iAdaptiveLearningQuizQuestionAnswerVOS cannot be null");
        // This is because True/False question has only 2 possible answer types.
        Assert.isTrue(iAdaptiveLearningQuizQuestionAnswerVOS.size() >= 2, "Question answers size should at least be 2");
        IAdaptiveLearningQuizQuestionAnswerVO[] quizQuestionAnswersArray = iAdaptiveLearningQuizQuestionAnswerVOS.toArray(new IAdaptiveLearningQuizQuestionAnswerVO[iAdaptiveLearningQuizQuestionAnswerVOS.size()]);

        IAdaptiveLearningQuizQuestionAnswerVO option1AnswerModel = quizQuestionAnswersArray[0];
        option1 = option1AnswerModel.getQuizQuestionAnswerText();
        if(option1AnswerModel.isCorrectAnswer()) {
            correctAnswer1 = option1AnswerModel.getQuizQuestionAnswerText();
        }

        IAdaptiveLearningQuizQuestionAnswerVO option2AnswerModel = quizQuestionAnswersArray[1];
        option2 = option2AnswerModel.getQuizQuestionAnswerText();
        if(option2AnswerModel.isCorrectAnswer()) {
            correctAnswer1 = option2AnswerModel.getQuizQuestionAnswerText();
        }

        // True/False question answers will only have 2 while everything else will have 4 answers
        if (quizQuestionAnswersArray.length > 2) {
            IAdaptiveLearningQuizQuestionAnswerVO option3AnswerModel = quizQuestionAnswersArray[2];
            option3 = option3AnswerModel.getQuizQuestionAnswerText();
            if(option3AnswerModel.isCorrectAnswer()) {
                correctAnswer1 = option3AnswerModel.getQuizQuestionAnswerText();
            }

            IAdaptiveLearningQuizQuestionAnswerVO option4AnswerModel = quizQuestionAnswersArray[3];
            option4 = option4AnswerModel.getQuizQuestionAnswerText();
            if(option4AnswerModel.isCorrectAnswer()) {
                correctAnswer1 = option4AnswerModel.getQuizQuestionAnswerText();
            }
        }
    }

    public Set<IAdaptiveLearningQuizQuestionAnswerVO> getQuizQuestionAnswers(AdaptiveLearningQuizQuestionTypeE adaptiveLearningQuizQuestionTypeE) {
        Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS = new LinkedHashSet<>();

        addOption1Answer(iAdaptiveLearningQuizQuestionAnswerVOS);
        addOption2Answer(iAdaptiveLearningQuizQuestionAnswerVOS);

        if (adaptiveLearningQuizQuestionTypeE != AdaptiveLearningQuizQuestionTypeE.True_False) {
            // Options 3 and 4 should be added for all Question Types except the True/False option
            addOption3Answer(iAdaptiveLearningQuizQuestionAnswerVOS);
            addOption4Answer(iAdaptiveLearningQuizQuestionAnswerVOS);
        }

        return iAdaptiveLearningQuizQuestionAnswerVOS;
    }

    private void addOption1Answer(Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS) {
        if(option1 != null && option1.length() > 0) {
            AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO1 = new AdaptiveLearningQuizQuestionAnswerVO();
            adaptiveLearningQuizQuestionAnswerVO1.setQuizQuestionAnswerText(option1);
            adaptiveLearningQuizQuestionAnswerVO1.setCorrectAnswer("option1".equals(correctAnswer1));
            iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO1);
        }
    }

    private void addOption2Answer(Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS) {
        if(option2 != null && option2.length() > 0) {
            AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO2 = new AdaptiveLearningQuizQuestionAnswerVO();
            adaptiveLearningQuizQuestionAnswerVO2.setQuizQuestionAnswerText(option2);
            adaptiveLearningQuizQuestionAnswerVO2.setCorrectAnswer("option2".equals(correctAnswer1));
            iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO2);
        }
    }

    private void addOption3Answer(Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS) {
        if(option3 != null && option3.length() > 0) {
            AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO3 = new AdaptiveLearningQuizQuestionAnswerVO();
            adaptiveLearningQuizQuestionAnswerVO3.setQuizQuestionAnswerText(option3);
            adaptiveLearningQuizQuestionAnswerVO3.setCorrectAnswer("option3".equals(correctAnswer1));
            iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO3);
        }
    }

    private void addOption4Answer(Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS) {
        if(option4 != null && option4.length() > 0) {
            AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO4 = new AdaptiveLearningQuizQuestionAnswerVO();
            adaptiveLearningQuizQuestionAnswerVO4.setQuizQuestionAnswerText(option4);
            adaptiveLearningQuizQuestionAnswerVO4.setCorrectAnswer("option4".equals(correctAnswer1));
            iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO4);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdaptiveQuizQuestionAnswerModel that = (AdaptiveQuizQuestionAnswerModel) o;

        return new EqualsBuilder()
                .append(option1, that.option1)
                .append(option2, that.option2)
                .append(option3, that.option3)
                .append(option4, that.option4)
                .append(correctAnswer1, that.correctAnswer1)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(option1)
                .append(option2)
                .append(option3)
                .append(option4)
                .append(correctAnswer1)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("option1", option1)
                .append("option2", option2)
                .append("option3", option3)
                .append("option4", option4)
                .append("correctAnswer1", correctAnswer1)
                .toString();
    }
}
