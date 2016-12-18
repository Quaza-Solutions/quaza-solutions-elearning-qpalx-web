package com.quaza.solutions.qpalx.elearning.web.sstatic.utils;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;

import java.io.File;
import java.util.Optional;

/**
 * @author manyce400
 */
public interface IStaticContentMediaUtils {

    public Optional<MediaContentTypeE> getMediaContentType(String mediaContentFileName);

    public ELearningMediaContent buildELearningMediaContent(File mediaContentFile, QPalXTutorialContentTypeE qPalXTutorialContentTypeE);
}
