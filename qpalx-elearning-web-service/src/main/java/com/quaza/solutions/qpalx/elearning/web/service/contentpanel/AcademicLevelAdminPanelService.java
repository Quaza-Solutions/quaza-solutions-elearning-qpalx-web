package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.ContentAdminProfileHolder;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialLevel;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.CacheEnabledELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.web.security.login.WebQPalXUser;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author manyce400
 */
@Service(AcademicLevelAdminPanelService.BEAN_NAME)
public class AcademicLevelAdminPanelService implements IAcademicLevelAdminPanelService {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXTutorialService")
    private IQPalXTutorialService iqPalXTutorialService;

    @Autowired
    @Qualifier(CacheEnabledELearningCurriculumService.BEAN_NAME)
    private IELearningCurriculumService ieLearningCurriculumService;

    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.contentpanel.AcademicLevelAdminPanelService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AcademicLevelAdminPanelService.class);



    @Override
    public void addAdministratorAcademicGradeLevels(Model model, CurriculumType curriculumType, StudentTutorialGrade studentTutorialGrade) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(curriculumType, "curriculumType cannot be null");
        Assert.notNull(studentTutorialGrade, "studentTutorialLevel cannot be null");

        // Get the current logged in Content Developer user and find all StudentTutorialGrades which user can create content for.
        WebQPalXUser webQPalXUser = iqPalXUserWebService.getWebQPalXUser();
        QPalXUser qPalXUser = webQPalXUser.getQPalXUser();
        ContentAdminProfileHolder contentAdminProfileHolder = webQPalXUser.getContentAdminProfileHolder().get();

        if (contentAdminProfileHolder != null && qPalXUser.getUserType() == QPalxUserTypeE.CONTENT_DEVELOPER) {
            LOGGER.info("Adding Tutorial Grades that Content Admin:> {} is enabled for...", qPalXUser.getEmail());
            model.addAttribute(StudentTutorialGrade.CLASS_INSTANCES_IDENTIFIER, contentAdminProfileHolder.getStudentTutorialGrades());

            // Add the TutorialLevel and Grade that this Content Admin has been assigned to
            model.addAttribute(StudentTutorialLevel.CLASS_ATTRIBUTE_IDENTIFIER, contentAdminProfileHolder.getContentAdminProfileRecord().getStudentTutorialLevel());
            model.addAttribute(StudentTutorialGrade.CLASS_ATTRIBUTE_IDENTIFIER, studentTutorialGrade);

            // Get all the Curricula for passed in curriculumType
            List<ELearningCurriculum> eLearningCurricula = ieLearningCurriculumService.findAllCurriculumByTutorialGradeAndType(curriculumType, studentTutorialGrade);
            model.addAttribute(ELearningCurriculum.CLASS_INSTANCES_IDENTIFIER, eLearningCurricula);
        }
    }

    @Override
    public StudentTutorialGrade findBaseAdminAssignedStudentTutorialGrade() {
        LOGGER.info("Finding and returning the base assigned StudentTutorialGrade for Content Admin...");
        WebQPalXUser webQPalXUser = iqPalXUserWebService.getWebQPalXUser();
        ContentAdminProfileHolder contentAdminProfileHolder = webQPalXUser.getContentAdminProfileHolder().get();
        List<StudentTutorialGrade> studentTutorialGrades = contentAdminProfileHolder.getStudentTutorialGrades();
        return studentTutorialGrades.get(0);
    }
}
