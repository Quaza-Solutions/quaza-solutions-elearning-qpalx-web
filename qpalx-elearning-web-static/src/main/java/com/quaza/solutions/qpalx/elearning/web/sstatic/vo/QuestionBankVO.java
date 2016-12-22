package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.scorable.IQuestionBankVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.AbstractILMSMediaContentVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;

import java.util.Set;

/**
 * @author manyce400
 */
public class QuestionBankVO extends AbstractILMSMediaContentVO implements IQuestionBankVO {



    private String questionTitle;

    private String questionDescription;

    private Long qPalXELessonID;


    public QuestionBankVO() {

    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public Long getQPalXELessonID() {
        return qPalXELessonID;
    }

    public void setQPalXELessonID(Long qPalXELessonID) {
        this.qPalXELessonID = qPalXELessonID;
    }

    @Override
    public Set<MediaContentTypeE> getMediaContentTypes() {
        return ImmutableSet.of(MediaContentTypeE.text);
    }

    @Override
    public Set<QPalXTutorialContentTypeE> getQPalXTutorialContentTypes() {
        return ImmutableSet.of(QPalXTutorialContentTypeE.Assignment);
    }

    @Override
    public IHierarchicalLMSContent getIHierarchicalLMSContent() {
        return null;
    }

}
