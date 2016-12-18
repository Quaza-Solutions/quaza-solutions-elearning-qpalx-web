package com.quaza.solutions.qpalx.elearning.web.sstatic.utils;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author manyce400
 */
public interface IStaticContentFileUtils {


    public String getUniqueSafeFileName(MultipartFile multipartFile);

}
