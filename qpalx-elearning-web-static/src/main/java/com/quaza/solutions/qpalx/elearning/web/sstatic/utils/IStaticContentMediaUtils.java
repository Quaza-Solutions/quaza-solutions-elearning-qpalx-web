package com.quaza.solutions.qpalx.elearning.web.sstatic.utils;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.ILMSMediaContentVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.sstatic.content.StaticContentConfigurationTypeE;

import java.io.File;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IStaticContentMediaUtils {

    public Optional<MediaContentTypeE> getMediaContentType(String mediaContentFileName);

    public String getELearningMediaUploadLocation(ILMSMediaContentVO ilmsMediaContentVO);

    public String getELearningMediaPhysicalFileLocation(ELearningMediaContent eLearningMediaContent, StaticContentConfigurationTypeE staticContentConfigurationTypeE);

    public ELearningMediaContent buildELearningMediaContent(File mediaContentFile, QPalXTutorialContentTypeE qPalXTutorialContentTypeE, StaticContentConfigurationTypeE staticContentConfigurationTypeE);

    public ELearningMediaContent buildELearningMediaContent(String fileName, String filePath, QPalXTutorialContentTypeE qPalXTutorialContentTypeE, StaticContentConfigurationTypeE staticContentConfigurationTypeE);
}
