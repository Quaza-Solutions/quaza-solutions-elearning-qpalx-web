package com.quaza.solutions.qpalx.elearning.web.sstatic.utils;

import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.HierarchicalLMSContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.ILMSMediaContentVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.sstatic.content.StaticContentConfiguration;
import com.quaza.solutions.qpalx.elearning.domain.sstatic.content.StaticContentConfigurationTypeE;
import com.quaza.solutions.qpalx.elearning.service.sstatic.content.IStaticContentConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.sstatic.StaticContentMediaUtils")
public class StaticContentMediaUtils implements IStaticContentMediaUtils {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StaticContentConfigurationService")
    private IStaticContentConfigurationService iStaticContentConfigurationService;


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
    public String getELearningMediaUploadLocation(ILMSMediaContentVO ilmsMediaContentVO) {
        Assert.notNull(ilmsMediaContentVO, "ilmsMediaContentVO cannot be null");

        StaticContentConfiguration defaultELearningStaticContentConfiguration = iStaticContentConfigurationService.findStaticContentConfigurationByContentName(StaticContentConfigurationTypeE.ELearningContent);

        if(defaultELearningStaticContentConfiguration != null) {
            StringBuffer uploadLocation = new StringBuffer(defaultELearningStaticContentConfiguration.getStaticContentPhysicalLocation());

            // Get the hierarchical content and navigate up till root
            IHierarchicalLMSContent iHierarchicalLMSContent = ilmsMediaContentVO.getIHierarchicalLMSContent();
            LinkedList<IHierarchicalLMSContent> hierarchies = getContentHierarchies(iHierarchicalLMSContent, new LinkedList<IHierarchicalLMSContent>());

            // This is the actual content that we will be uploading static media content for, we need to determine if there would be more under this.
            IHierarchicalLMSContent levelForMediaUpload = hierarchies.getFirst();

            Iterator<IHierarchicalLMSContent> iterator = hierarchies.descendingIterator();
            while (iterator.hasNext()) {
                IHierarchicalLMSContent content = iterator.next();
                // replace all space in name to make a safe directory
                String safeContentDirectoryName = content.getHierarchicalLMSContentName().replace(" ", "-");

                boolean uploadMediaUnderLevel = isStaticMediaContentCreatedUnderContent(content);
                if(uploadMediaUnderLevel) {
                    uploadLocation.append(safeContentDirectoryName).append("/");
                } else {
                    break;
                }

//                if (counter != totalHierarchies) {
//                    uploadLocation.append(safeContentDirectoryName).append("/");
//                    counter++;
//                } else {
//                    boolean uploadMediaUnderLevel = isStaticMediaContentCreatedUnderContent(content);
//                    if(uploadMediaUnderLevel) {
//                        uploadLocation.append(safeContentDirectoryName).append("/");
//                    }
//                }
            }

            return uploadLocation.toString();
        }

        throw new UnsupportedOperationException("Location for static ELearningContent was not found");
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

    protected LinkedList<IHierarchicalLMSContent> getContentHierarchies(IHierarchicalLMSContent iHierarchicalLMSContent, LinkedList<IHierarchicalLMSContent> hierarchies) {
        IHierarchicalLMSContent parentContent = iHierarchicalLMSContent.getIHierarchicalLMSContentParent();

        // Check for end of recursion
        if(parentContent == null) {
            hierarchies.add(iHierarchicalLMSContent);
            return hierarchies;
        }

        hierarchies.add(iHierarchicalLMSContent);
        return getContentHierarchies(iHierarchicalLMSContent.getIHierarchicalLMSContentParent(), hierarchies);
    }

    protected boolean isStaticMediaContentCreatedUnderContent(IHierarchicalLMSContent staticContentForMediaUpload) {
        HierarchicalLMSContentTypeE hierarchicalLMSContentTypeE = staticContentForMediaUpload.getHierarchicalLMSContentTypeE();
        boolean answer;

        switch (hierarchicalLMSContentTypeE) {
            case AdaptiveQuiz:
                answer = false;
                break;
            case MicroLesson:
                answer = false;
                break;
            case QuestionBank:
                answer = false;
                break;
            case Lesson:
                answer = true;
                break;
            case ELearningCourse:
                answer =true;
                break;
            case ELearningCurriculum:
                answer = true;
                break;
            default:
                answer = false;
                break;
        }

        return answer;
    }

}
