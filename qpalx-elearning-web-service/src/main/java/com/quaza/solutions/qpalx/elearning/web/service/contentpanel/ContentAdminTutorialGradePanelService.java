package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.google.common.collect.ImmutableMap;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCourse;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdminTutorialGradePanelE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IWebAttributesUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * Implementation of IContentAdminTutorialGradePanelService which creates attributes for Admin tutorial panel.
 *
 * @author manyce400
 */
@Service(ContentAdminTutorialGradePanelService.BEAN_NAME)
public class ContentAdminTutorialGradePanelService implements IContentAdminTutorialGradePanelService {


    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.WebAttributesUtil")
    private IWebAttributesUtil iWebAttributesUtil;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXTutorialService")
    private IQPalXTutorialService iqPalXTutorialService;

    public static final String BEAN_NAME = "quaza.solutions.qpalx.elearning.web.ContentAdminTutorialGradePanelService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ContentAdminTutorialGradePanelService.class);


    @Override
    public void addDisplayPanelAttributes(Model model, Boolean addLessonsEnabled, Boolean addMicroLessonsEnabled, Boolean addMicroLessonsActivitiesEnabled, ELearningCourse eLearningCourse) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(addLessonsEnabled, "addLessonsEnabled cannot be null");
        Assert.notNull(addMicroLessonsEnabled, "addMicroLessonsEnabled cannot be null");
        Assert.notNull(addMicroLessonsActivitiesEnabled, "addMicroLessonsActivitiesEnabled cannot be null");
        Assert.notNull(eLearningCourse, "eLearningCourse cannot be null");

        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        String studentTutorialGradeID = eLearningCurriculum.getStudentTutorialGrade().getId().toString();
        String curriculumType = eLearningCurriculum.getCurriculumType().toString();

        // Add selected attributes
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCurriculum.toString(), eLearningCurriculum);
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);

        // add all web attributes
        CurriculumType curriculumTypeE = CurriculumType.valueOf(curriculumType);
        StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(NumberUtils.toLong(studentTutorialGradeID));
        ImmutableMap<String, Object> webAttributes = AdminTutorialGradePanelE.buildAttributes(addLessonsEnabled, addMicroLessonsEnabled, addMicroLessonsActivitiesEnabled, studentTutorialGrade, curriculumType.toString());
        addWebAttributes(model, webAttributes);
    }

    @Override
    public void addDisplayPanelAttributes(Model model, Boolean addLessonsEnabled, Boolean addMicroLessonsEnabled, Boolean addMicroLessonsActivitiesEnabled, QPalXELesson qPalXELesson) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(addLessonsEnabled, "addLessonsEnabled cannot be null");
        Assert.notNull(addMicroLessonsEnabled, "addMicroLessonsEnabled cannot be null");
        Assert.notNull(addMicroLessonsActivitiesEnabled, "addMicroLessonsActivitiesEnabled cannot be null");
        Assert.notNull(qPalXELesson, "qPalXELesson cannot be null");

        ELearningCourse eLearningCourse = qPalXELesson.geteLearningCourse();
        ELearningCurriculum eLearningCurriculum = eLearningCourse.geteLearningCurriculum();
        String studentTutorialGradeID = eLearningCurriculum.getStudentTutorialGrade().getId().toString();
        String curriculumType = eLearningCurriculum.getCurriculumType().toString();

        // Add selected attributes
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCurriculum.toString(), eLearningCurriculum);
        model.addAttribute(CurriculumDisplayAttributeE.SelectedELearningCourse.toString(), eLearningCourse);
        model.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXELesson.toString(), qPalXELesson);

        // add all web attributes
        CurriculumType curriculumTypeE = CurriculumType.valueOf(curriculumType);
        StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(NumberUtils.toLong(studentTutorialGradeID));
        ImmutableMap<String, Object> webAttributes = AdminTutorialGradePanelE.buildAttributes(addLessonsEnabled, addMicroLessonsEnabled, addMicroLessonsActivitiesEnabled, studentTutorialGrade, curriculumType.toString());
        addWebAttributes(model, webAttributes);
    }

    @Override
    public void addDisplayPanelAttributes(Model model, Boolean addCoursesEnabled, Boolean addCourseActivitiesEnabled, String studentTutorialGradeID, String curriculumType) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(addCoursesEnabled, "addCoursesEnabled cannot be null");
        Assert.notNull(addCourseActivitiesEnabled, "addCourseActivitiesEnabled cannot be null");
        Assert.notNull(studentTutorialGradeID, "studentTutorialGradeID cannot be null");
        Assert.notNull(curriculumType, "curriculumType cannot be null");

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        if(optionalUser.isPresent()) {
            LOGGER.debug("Adding all attributes required for admin tutorial grade panel for user:> {}", optionalUser.get().getEmail());
            CurriculumType curriculumTypeE = CurriculumType.valueOf(curriculumType);
            StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(NumberUtils.toLong(studentTutorialGradeID));
            ImmutableMap<String, Object> attributes = AdminTutorialGradePanelE.buildAttributes(addCoursesEnabled, addCourseActivitiesEnabled, studentTutorialGrade, curriculumType.toString());
            iWebAttributesUtil.addWebAttributes(model, attributes);
        }

    }

    private void addWebAttributes(Model model, ImmutableMap<String, Object> webAttributes) {
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        if(optionalUser.isPresent()) {
            LOGGER.debug("Adding all webAttributes required for admin tutorial grade panel for user:> {}", optionalUser.get().getEmail());
            iWebAttributesUtil.addWebAttributes(model, webAttributes);
        }
    }

}
