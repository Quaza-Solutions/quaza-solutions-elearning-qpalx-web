package com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.ILMSMediaContentVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.sstatic.content.StaticContentConfigurationTypeE;
import com.quaza.solutions.qpalx.elearning.web.sstatic.utils.IStaticContentFileUtils;
import com.quaza.solutions.qpalx.elearning.web.sstatic.utils.IStaticContentMediaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(ELearningStaticContentService.BEAN_NAME)
public class ELearningStaticContentService implements IELearningStaticContentService {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.StaticContentFileUtils")
    private IStaticContentFileUtils iStaticContentFileUtils;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.StaticContentMediaUtils")
    private IStaticContentMediaUtils iStaticContentMediaUtils;

    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ELearningStaticContentService.class);


    @Override
    public void deleteELearningMediaContent(ELearningMediaContent eLearningMediaContent) {
        Assert.notNull(eLearningMediaContent, "eLearningMediaContent cannot be null");
        Assert.notNull(eLearningMediaContent.getELearningMediaPhysicalFile(), "ELearningMediaPhysicalFile path cannot be null");

        String pathLocationWithFileName = eLearningMediaContent.getELearningMediaPhysicalFile();

        LOGGER.info("Deleting ELearning content with fileName:> {}", pathLocationWithFileName);
        File file = new File(pathLocationWithFileName);
        file.delete();
    }

    @Override
    public ELearningMediaContent uploadELearningMediaContent(MultipartFile multipartFile, ILMSMediaContentVO ilmsMediaContentVO) {
        Assert.notNull(multipartFile, "multipartFile cannot be null");
        Assert.notNull(ilmsMediaContentVO, "ilmsMediaContentVO cannot be null");
        Assert.notNull(ilmsMediaContentVO.getIHierarchicalLMSContent(), "parent content is required for ilmsMediaContentVO");

        LOGGER.info("Uploading new static ELearning media content file with parent: {}", ilmsMediaContentVO.getIHierarchicalLMSContent());

        // Get new safe file name to use for upload
        String safeFileName = iStaticContentFileUtils.getUniqueSafeFileName(multipartFile);
        Optional<MediaContentTypeE> optional = iStaticContentMediaUtils.getMediaContentType(safeFileName);
        boolean mediaTypeSupported = optional.isPresent() ? ilmsMediaContentVO.getMediaContentTypes().contains(optional.get()) : false;

        if(mediaTypeSupported) {
            String fileUploadLocation = iStaticContentMediaUtils.getELearningMediaUploadLocation(ilmsMediaContentVO);
            LOGGER.info("File with name: {} will be uploaded to:> {}", safeFileName, fileUploadLocation);

            File mediaContentFile = writeFileToDisk(multipartFile, safeFileName, fileUploadLocation);
            if (mediaContentFile != null) {
                ELearningMediaContent eLearningMediaContent = iStaticContentMediaUtils.buildELearningMediaContent(mediaContentFile, ilmsMediaContentVO.getSelectedQPalXTutorialContentTypeE(), StaticContentConfigurationTypeE.ELearningContent);
                return eLearningMediaContent;
            }

            LOGGER.warn("Failed to upload file");
            return null;
        }

        LOGGER.warn("File:> {} is not supported by this media content type on QPalx platform", safeFileName);
        return null;
    }


    private File writeFileToDisk(MultipartFile multipartFile, String uniqueSafefileName, String fileLocation) {
        String fileName = uniqueSafefileName;

        try {
            // First we need to upload and output to local directory
            byte[] bytes = multipartFile.getBytes();
            String newFileName = fileLocation + fileName;
            new File(newFileName).getParentFile().mkdirs();
            LOGGER.info("Writing new file with name: {} to output stream", fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(newFileName)));
            stream.write(bytes);
            stream.close();
            return new File(newFileName);
        } catch (Exception e) {
            LOGGER.error("Exception occurred while uploading file.  File could not be uploaded", e);
            return null;
        }
    }
}
