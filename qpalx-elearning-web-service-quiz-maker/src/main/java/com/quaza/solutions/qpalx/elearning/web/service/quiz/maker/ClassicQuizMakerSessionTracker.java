package com.quaza.solutions.qpalx.elearning.web.service.quiz.maker;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.quiz.IAdaptiveLearningQuizVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.web.sstatic.domain.EmbedableQuizSetting;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;

/**
 * @author manyce400
 */
@Service(ClassicQuizMakerSessionTracker.BEAN_NAME)
public class ClassicQuizMakerSessionTracker implements IClassicQuizMakerSessionTracker {



    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.quiz.maker.ClassicQuizMakerSessionTracker";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClassicQuizMakerSessionTracker.class);


    @Override
    public void startAdaptiveQuizCreation(ModelMap modelMap, IHierarchicalLMSContent iHierarchicalLMSContent, IAdaptiveLearningQuizVO iAdaptiveLearningQuizVO) {
        Assert.notNull(modelMap, "modelMap cannot be null");
        Assert.notNull(iHierarchicalLMSContent, "iHierarchicalLMSContent cannot be null");
        Assert.notNull(iAdaptiveLearningQuizVO, "iAdaptiveLearningQuizVO cannot be null");

        LOGGER.info("Tracking start of new AdaptiveQuiz creation for iHierarchicalLMSContent: {}", iHierarchicalLMSContent);

        // Add to modelMap session
        modelMap.addAttribute(AdaptiveLearningQuizWebVO.CLASS_ATTRIBUTE, iAdaptiveLearningQuizVO);
        modelMap.addAttribute(IHierarchicalLMSContent.CLASS_ATTRIBUTE_IDENTIFIER, iHierarchicalLMSContent);

        // Add quiz settings to model
        LOGGER.debug("Adding embeded quiz settings to model from: {}", iHierarchicalLMSContent);
        EmbedableQuizSetting embedableQuizSetting = new EmbedableQuizSetting(iHierarchicalLMSContent);
        embedableQuizSetting.addQuizSettingsToModel(modelMap);
    }


}
