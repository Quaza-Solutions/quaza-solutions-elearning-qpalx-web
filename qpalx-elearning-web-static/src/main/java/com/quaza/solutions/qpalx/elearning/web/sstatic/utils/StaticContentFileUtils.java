package com.quaza.solutions.qpalx.elearning.web.sstatic.utils;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.sstatic.StaticContentFileUtils")
public class StaticContentFileUtils implements IStaticContentFileUtils {



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StaticContentFileUtils.class);


    @Override
    public String getUniqueSafeFileName(MultipartFile multipartFile) {
        Assert.notNull(multipartFile, "multipartFile cannot be null");
        Assert.notNull(multipartFile.getOriginalFilename(), "multipartFile filen name cannot be null");
        LOGGER.debug("Generating unique safe file name for multipartfile...");

        // Replace all spaces with underscore
        String newFileName = multipartFile.getOriginalFilename().replace(" ", "_");
        String actualName = newFileName.substring(0, newFileName.indexOf("."));
        String fileExtension = newFileName.substring(newFileName.indexOf(".") + 1);

        // This will append current time in millis to make sure that file name will always be unique.
        return new StringBuffer()
                .append(actualName)
                .append("_")
                .append(System.currentTimeMillis())
                .append(".")
                .append(fileExtension)
                .toString();
    }
}
