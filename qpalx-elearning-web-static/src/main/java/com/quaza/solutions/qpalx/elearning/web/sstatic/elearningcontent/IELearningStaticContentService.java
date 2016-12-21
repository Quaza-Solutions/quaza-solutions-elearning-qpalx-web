package com.quaza.solutions.qpalx.elearning.web.sstatic.elearningcontent;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningMediaContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.ILMSMediaContentVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author manyce400
 */
public interface IELearningStaticContentService {


    public void deleteELearningMediaContent(ELearningMediaContent eLearningMediaContent);

    public ELearningMediaContent uploadELearningMediaContent(MultipartFile multipartFile, ILMSMediaContentVO ilmsMediaContentVO);


}
