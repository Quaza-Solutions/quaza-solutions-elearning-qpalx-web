package com.quaza.solutions.qpalx.elearning.web.sstatic.domain;

import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.ui.ModelMap;

/**
 * @author manyce400
 */
public class EmbedableQuizSetting {




    private IHierarchicalLMSContent iHierarchicalLMSContent;

    public static final String QUIZ_PARENT_LMS_CONTENT_NAME = "AdaptiveQuizParentContentName";

    public static final String QUIZ_PARENT_LMS_CONTENT_TYPE = "AdaptiveQuizParentContentType";


    public EmbedableQuizSetting(IHierarchicalLMSContent iHierarchicalLMSContent) {
        this.iHierarchicalLMSContent = iHierarchicalLMSContent;
    }

    public IHierarchicalLMSContent getIHierarchicalLMSContent() {
        return iHierarchicalLMSContent;
    }

    public void setIHierarchicalLMSContent(IHierarchicalLMSContent iHierarchicalLMSContent) {
        this.iHierarchicalLMSContent = iHierarchicalLMSContent;
    }

    public void addQuizSettingsToModel(ModelMap modelMap) {
        modelMap.addAttribute(QUIZ_PARENT_LMS_CONTENT_NAME, iHierarchicalLMSContent.getHierarchicalLMSContentName());
        modelMap.addAttribute(QUIZ_PARENT_LMS_CONTENT_TYPE, iHierarchicalLMSContent.getHierarchicalLMSContentTypeE());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        EmbedableQuizSetting that = (EmbedableQuizSetting) o;

        return new EqualsBuilder()
                .append(iHierarchicalLMSContent, that.iHierarchicalLMSContent)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(iHierarchicalLMSContent)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("iHierarchicalLMSContent", iHierarchicalLMSContent)
                .toString();
    }

    public static EmbedableQuizSetting newInstance(IHierarchicalLMSContent iHierarchicalLMSContent) {
        return new EmbedableQuizSetting(iHierarchicalLMSContent);
    }


}
