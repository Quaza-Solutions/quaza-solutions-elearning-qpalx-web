package com.quaza.solutions.qpalx.elearning.web.sstatic.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticContentFileUtilsTest {


    @InjectMocks
    private StaticContentFileUtils staticContentFileUtils;


    @Test
    public void testGetUniqueSafeFileName() {
        MultipartFile multipartFile = new MockMultipartFile("chemistry discipline.mp4", "chemistry discipline.mp4", "mp4", new byte[]{});
        String safeFileName = staticContentFileUtils.getUniqueSafeFileName(multipartFile);

        Assert.assertNotNull(safeFileName);
        Assert.assertNotEquals(multipartFile.getOriginalFilename(), safeFileName);
        
        int lastIndexOfUnderScore = safeFileName.lastIndexOf("_") + 1;
        String prefix = safeFileName.substring(0, lastIndexOfUnderScore);
        Assert.assertEquals("chemistry_discipline_", prefix);
        
        int indexOfExtStart = safeFileName.indexOf(".") + 1;
        String ext = safeFileName.substring(indexOfExtStart);
        Assert.assertEquals("mp4", ext);
    }
}
