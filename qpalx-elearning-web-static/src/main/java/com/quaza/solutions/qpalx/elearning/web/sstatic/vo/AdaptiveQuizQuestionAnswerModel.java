package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizQuestionAnswerVO;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author manyce400
 */
public abstract class AdaptiveQuizQuestionAnswerModel {



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

    public Set<IAdaptiveLearningQuizQuestionAnswerVO> getQuizQuestionAnswers() {
        Set<IAdaptiveLearningQuizQuestionAnswerVO> iAdaptiveLearningQuizQuestionAnswerVOS = new LinkedHashSet<>();

        // Build and add option1 answer
        AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO1 = new AdaptiveLearningQuizQuestionAnswerVO();
        adaptiveLearningQuizQuestionAnswerVO1.setQuizQuestionAnswerText(option1);
        adaptiveLearningQuizQuestionAnswerVO1.setCorrectAnswer("option1".equals(correctAnswer1));
        iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO1);

        // Build and add option2 answer
        AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO2 = new AdaptiveLearningQuizQuestionAnswerVO();
        adaptiveLearningQuizQuestionAnswerVO2.setQuizQuestionAnswerText(option2);
        adaptiveLearningQuizQuestionAnswerVO2.setCorrectAnswer("option2".equals(correctAnswer1));
        iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO2);

        // Build and add option3 answer
        AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO3 = new AdaptiveLearningQuizQuestionAnswerVO();
        adaptiveLearningQuizQuestionAnswerVO3.setQuizQuestionAnswerText(option3);
        adaptiveLearningQuizQuestionAnswerVO3.setCorrectAnswer("option3".equals(correctAnswer1));
        iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO3);

        // Build and add option4 answer
        AdaptiveLearningQuizQuestionAnswerVO adaptiveLearningQuizQuestionAnswerVO4 = new AdaptiveLearningQuizQuestionAnswerVO();
        adaptiveLearningQuizQuestionAnswerVO4.setQuizQuestionAnswerText(option4);
        adaptiveLearningQuizQuestionAnswerVO4.setCorrectAnswer("option4".equals(correctAnswer1));
        iAdaptiveLearningQuizQuestionAnswerVOS.add(adaptiveLearningQuizQuestionAnswerVO4);

        return iAdaptiveLearningQuizQuestionAnswerVOS;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("option1", option1)
                .append("option2", option2)
                .append("option3", option3)
                .append("option4", option4)
                .append("correctAnswer1", correctAnswer1)
                .toString();
    }
}
