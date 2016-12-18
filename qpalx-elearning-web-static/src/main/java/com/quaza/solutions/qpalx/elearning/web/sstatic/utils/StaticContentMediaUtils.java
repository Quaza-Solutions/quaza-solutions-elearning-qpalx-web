package com.quaza.solutions.qpalx.elearning.web.sstatic.utils;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.sstatic.StaticContentMediaUtils")
public class StaticContentMediaUtils implements IStaticContentMediaUtils {





    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StaticContentMediaUtils.class);


    @Override
    public Optional<MediaContentTypeE> getMediaContentType(String mediaContentFileName) {
        Assert.notNull(mediaContentFileName, "mediaContentFileName cannot be null");
        LOGGER.debug("Retrieving media content type for file: {}", mediaContentFileName);

        if(mediaContentFileName.lastIndexOf(".") != -1 && mediaContentFileName.lastIndexOf(".") != 0) {
            String fileType = mediaContentFileName.substring(mediaContentFileName.lastIndexOf(".")+1);
            try {
                return Optional.of(MediaContentTypeE.valueOf(fileType));
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Could not find matching media content type for file: {}", mediaContentFileName);
            }
        }

        return Optional.empty();
    }

    @Override
    public ELearningMediaContent buildELearningMediaContent(File mediaContentFile, QPalXTutorialContentTypeE qPalXTutorialContentTypeE) {
        Assert.notNull(mediaContentFile, "mediaContentFile cannot be null");
        Assert.notNull(qPalXTutorialContentTypeE, "qPalXTutorialContentTypeE cannot be null");

        LOGGER.debug("Creating new ELearningMediaContent from file: {}", mediaContentFile);

        // Get the file extension to figure out the media content type
        Optional<MediaContentTypeE> optionalMediaContentType = getMediaContentType(mediaContentFile.getName());

        // We save file name using symbolic link directory as the actual file will get uploaded to a directory outside web app context
        String symbolicFileDirectory = null;//getMediaContentTypeVirtualDirectory(optionalMediaContentType.get(), qPalXTutorialContentTypeE);
        String symbolicFileName = symbolicFileDirectory + mediaContentFile.getName();

        return ELearningMediaContent.builder()
                .eLearningMediaType(optionalMediaContentType.get().toString())
                .qPalXTutorialContentTypeE(qPalXTutorialContentTypeE)
                .eLearningMediaFile(symbolicFileName)
                .build();
    }
}
