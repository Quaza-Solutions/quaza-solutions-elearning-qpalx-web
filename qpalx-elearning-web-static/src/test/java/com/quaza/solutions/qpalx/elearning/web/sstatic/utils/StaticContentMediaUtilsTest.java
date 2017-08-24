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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

/**
 * @author manyce400 
 */
@RunWith(MockitoJUnitRunner.class)
public class StaticContentMediaUtilsTest {
    

    @Mock
    private IStaticContentConfigurationService iStaticContentConfigurationService;
    
    @InjectMocks
    private StaticContentMediaUtils staticContentMediaUtils;

    @Before
    public void before() {
        StaticContentConfiguration staticContentConfiguration = new StaticContentConfiguration();
        staticContentConfiguration.setStaticContentPhysicalLocation("/Users/manyce400/QuazaSolutions/elearning-content/");
        staticContentConfiguration.setStaticContentApplicationContextLocation("/elearning-content/");
        Mockito.when(iStaticContentConfigurationService.findStaticContentConfigurationByContentName(StaticContentConfigurationTypeE.ELearningContent))
                .thenReturn(staticContentConfiguration);
    }
    
    
    @Test
    public void testGetMediaContentTypeMp4() {
        Optional<MediaContentTypeE> optional = staticContentMediaUtils.getMediaContentType("falafel.mp4");
        Assert.assertNotNull(optional);
        Assert.assertEquals(MediaContentTypeE.mp4, optional.get());
    }

    @Test
    public void testGetMediaContentTypeSwf() {
        Optional<MediaContentTypeE> optional = staticContentMediaUtils.getMediaContentType("falafel.swf");
        Assert.assertNotNull(optional);
        Assert.assertEquals(MediaContentTypeE.swf, optional.get());
    }

    @Test
    public void testGetMediaContentTypeHtml() {
        Optional<MediaContentTypeE> optional = staticContentMediaUtils.getMediaContentType("falafel.html");
        Assert.assertNotNull(optional);
        Assert.assertEquals(MediaContentTypeE.html, optional.get());
    }

    @Test
    public void testGetMediaContentTypeJpeg() {
        Optional<MediaContentTypeE> optional = staticContentMediaUtils.getMediaContentType("falafel.jpeg");
        Assert.assertNotNull(optional);
        Assert.assertEquals(MediaContentTypeE.jpeg, optional.get());
    }

    @Test
    public void testGetMediaContentTypePng() {
        Optional<MediaContentTypeE> optional = staticContentMediaUtils.getMediaContentType("falafel.png");
        Assert.assertNotNull(optional);
        Assert.assertEquals(MediaContentTypeE.png, optional.get());
    }

    @Test
    public void testGetMediaContentTypeUnSupported() {
        Optional<MediaContentTypeE> optional = staticContentMediaUtils.getMediaContentType("falafel.cptx");
        Assert.assertNotNull(optional);
        Assert.assertFalse(optional.isPresent());
    }


    @Test
    public void testGetContentHierarchies() {
        LessonIHierarchicalLMSContent lessonIHierarchicalLMSContent = new LessonIHierarchicalLMSContent();
        LinkedList<IHierarchicalLMSContent> hierarchies = staticContentMediaUtils.getContentHierarchies(lessonIHierarchicalLMSContent, new LinkedList<IHierarchicalLMSContent>());
        Assert.assertNotNull(hierarchies);
        Assert.assertTrue(hierarchies.size() == 3);
    }
    
    @Test
    public void testUploadLocationLesson() {
        LessonIHierarchicalLMSContent lessonIHierarchicalLMSContent = new LessonIHierarchicalLMSContent();
        TestLMSMediaContentVO testLMSMediaContentVO = new TestLMSMediaContentVO(lessonIHierarchicalLMSContent);
        String uploadLocation = staticContentMediaUtils.getELearningMediaUploadLocation(testLMSMediaContentVO);
        Assert.assertEquals("/Users/manyce400/QuazaSolutions/elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/", uploadLocation);
        System.out.println("Lesson uploadLocation = " + uploadLocation);
    }

    @Test
    public void testUploadLocationMicroLesson() {
        MicroLessonIHierarchicalLMSContent microLessonIHierarchicalLMSContent = new MicroLessonIHierarchicalLMSContent();
        TestLMSMediaContentVO testLMSMediaContentVO = new TestLMSMediaContentVO(microLessonIHierarchicalLMSContent);
        String uploadLocation = staticContentMediaUtils.getELearningMediaUploadLocation(testLMSMediaContentVO);
        Assert.assertEquals("/Users/manyce400/QuazaSolutions/elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/", uploadLocation);
        System.out.println("MicroLesson uploadLocation = " + uploadLocation);
    }

    @Test
    public void testUploadLocationAdaptiveQuiz() {
        AdaptiveQuizIHierarchicalLMSContent adaptiveQuizIHierarchicalLMSContent = new AdaptiveQuizIHierarchicalLMSContent();
        TestLMSMediaContentVO testLMSMediaContentVO = new TestLMSMediaContentVO(adaptiveQuizIHierarchicalLMSContent);
        String uploadLocation = staticContentMediaUtils.getELearningMediaUploadLocation(testLMSMediaContentVO);
        Assert.assertEquals("/Users/manyce400/QuazaSolutions/elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/", uploadLocation);
        System.out.println("AdaptiveQuiz uploadLocation = " + uploadLocation);
    }

    @Test
    public void testBuildELearningMediaContent() {
        File mediaContentFile = new File("/Users/manyce400/QuazaSolutions/elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/intro_1483995941079.mp4");
        ELearningMediaContent eLearningMediaContent = staticContentMediaUtils.buildELearningMediaContent(mediaContentFile, QPalXTutorialContentTypeE.Video, StaticContentConfigurationTypeE.ELearningContent);

        Assert.assertEquals("elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/intro_1483995941079.mp4", eLearningMediaContent.getELearningMediaFile());
        Assert.assertEquals("/Users/manyce400/QuazaSolutions/elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/intro_1483995941079.mp4", eLearningMediaContent.getELearningMediaPhysicalFile());
    }

    @Test
    public void testBuildELearningMediaContent2() {
        String fileName = "intro_1483995941079.mp4";
        String filePathWithName = "/Users/manyce400/QuazaSolutions/elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/intro_1483995941079.mp4";
        ELearningMediaContent eLearningMediaContent = staticContentMediaUtils.buildELearningMediaContent(fileName, filePathWithName, QPalXTutorialContentTypeE.Video, StaticContentConfigurationTypeE.ELearningContent);

        Assert.assertEquals("elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/intro_1483995941079.mp4", eLearningMediaContent.getELearningMediaFile());
        Assert.assertEquals("/Users/manyce400/QuazaSolutions/elearning-content/Science/Chemistry/Chemistry-as-a-Discipline/intro_1483995941079.mp4", eLearningMediaContent.getELearningMediaPhysicalFile());
    }


    // AdaptiveQuiz
    private final class AdaptiveQuizIHierarchicalLMSContent implements IHierarchicalLMSContent {

        private MicroLessonIHierarchicalLMSContent parent = new MicroLessonIHierarchicalLMSContent();

        @Override
        public String getHierarchicalLMSContentName() {
            return "Discipline 1 Quiz 1";
        }

        @Override
        public HierarchicalLMSContentTypeE getHierarchicalLMSContentTypeE() {
            return HierarchicalLMSContentTypeE.AdaptiveQuiz;
        }

        @Override
        public IHierarchicalLMSContent getIHierarchicalLMSContentParent() {
            return parent;
        }
    }


    // Micro Lesson
    private final class MicroLessonIHierarchicalLMSContent implements IHierarchicalLMSContent {

        private LessonIHierarchicalLMSContent parent = new LessonIHierarchicalLMSContent();

        @Override
        public String getHierarchicalLMSContentName() {
            return "Chemistry Discipline 1";
        }

        @Override
        public HierarchicalLMSContentTypeE getHierarchicalLMSContentTypeE() {
            return HierarchicalLMSContentTypeE.MicroLesson;
        }

        @Override
        public IHierarchicalLMSContent getIHierarchicalLMSContentParent() {
            return parent;
        }
    }


    // Lesson
    private final class LessonIHierarchicalLMSContent implements IHierarchicalLMSContent {

        private CourseIHierarchicalLMSContent parent = new CourseIHierarchicalLMSContent();

        @Override
        public String getHierarchicalLMSContentName() {
            return "Chemistry as a Discipline";
        }

        @Override
        public HierarchicalLMSContentTypeE getHierarchicalLMSContentTypeE() {
            return HierarchicalLMSContentTypeE.Lesson;
        }

        @Override
        public IHierarchicalLMSContent getIHierarchicalLMSContentParent() {
            return parent;
        }
    }

    private final class CourseIHierarchicalLMSContent implements IHierarchicalLMSContent {

        private CurriculumIHierarchicalLMSContent parent = new CurriculumIHierarchicalLMSContent();

        @Override
        public String getHierarchicalLMSContentName() {
            return "Chemistry";
        }

        @Override
        public HierarchicalLMSContentTypeE getHierarchicalLMSContentTypeE() {
            return HierarchicalLMSContentTypeE.ELearningCourse;
        }

        @Override
        public IHierarchicalLMSContent getIHierarchicalLMSContentParent() {
            return parent;
        }
    }

    private final class CurriculumIHierarchicalLMSContent implements IHierarchicalLMSContent {

        @Override
        public String getHierarchicalLMSContentName() {
            return "Science";
        }

        @Override
        public HierarchicalLMSContentTypeE getHierarchicalLMSContentTypeE() {
            return HierarchicalLMSContentTypeE.ELearningCurriculum;
        }

        @Override
        public IHierarchicalLMSContent getIHierarchicalLMSContentParent() {
            return null;
        }
    }

    private class TestLMSMediaContentVO implements ILMSMediaContentVO {

        private IHierarchicalLMSContent iHierarchicalLMSContent;

        public TestLMSMediaContentVO(IHierarchicalLMSContent iHierarchicalLMSContent) {
            this.iHierarchicalLMSContent = iHierarchicalLMSContent;
        }

        @Override
        public boolean isActive() {
            return false;
        }

        @Override
        public Set<MediaContentTypeE> getMediaContentTypes() {
            return null;
        }

        @Override
        public Set<QPalXTutorialContentTypeE> getQPalXTutorialContentTypes() {
            return null;
        }

        @Override
        public QPalXTutorialContentTypeE getSelectedQPalXTutorialContentTypeE() {
            return null;
        }

        @Override
        public ELearningMediaContent getELearningMediaContent() {
            return null;
        }

        @Override
        public IHierarchicalLMSContent getIHierarchicalLMSContent() {
            return iHierarchicalLMSContent;
        }
    }
}
