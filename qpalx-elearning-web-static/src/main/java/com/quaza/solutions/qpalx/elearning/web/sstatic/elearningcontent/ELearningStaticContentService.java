package com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.ILMSMediaContentVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.web.sstatic.utils.IStaticContentFileUtils;
import com.quaza.solutions.qpalx.elearning.web.sstatic.utils.IStaticContentMediaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.sstatic.ELearningStaticContentService")
public class ELearningStaticContentService implements IELearningStaticContentService {



    @Autowired
    @Qualifier("om.quaza.solutions.qpalx.elearning.web.sstatic.StaticContentFileUtils")
    private IStaticContentFileUtils iStaticContentFileUtils;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.sstatic.StaticContentMediaUtils")
    private IStaticContentMediaUtils iStaticContentMediaUtils;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ELearningStaticContentService.class);


    @Override
    public ELearningMediaContent uploadELearningMediaContent(MultipartFile multipartFile, ILMSMediaContentVO ilmsMediaContentVO) {
        Assert.notNull(multipartFile, "multipartFile cannot be null");
        Assert.notNull(ilmsMediaContentVO, "ilmsMediaContentVO cannot be null");
        Assert.notNull(ilmsMediaContentVO.getIHierarchicalLMSContent(), "parent content is required for ilmsMediaContentVO");

        LOGGER.info("Uploading new static ELearning media content file with parent: {}", ilmsMediaContentVO.getIHierarchicalLMSContent());

        // Get new safe file name to use for upload
        String safeFileName = iStaticContentFileUtils.getUniqueSafeFileName(multipartFile);
        Optional<MediaContentTypeE> optional = iStaticContentMediaUtils.getMediaContentType(safeFileName);

        return null;
    }
}
