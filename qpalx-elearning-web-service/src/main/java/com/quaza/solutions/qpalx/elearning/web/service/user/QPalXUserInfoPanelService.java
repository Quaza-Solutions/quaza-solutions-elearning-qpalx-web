package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.AdaptiveProficiencyRanking;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.StudentEnrolmentRecord;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialLevel;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.IAdaptiveProficiencyRankingService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.DefaultStudentEnrolmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IStudentEnrolmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.CacheEnabledQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.UserInfoPanelAttributesE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service(QPalXUserInfoPanelService.BEAN_NAME)
public class QPalXUserInfoPanelService implements IQPalXUserInfoPanelService {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultAdaptiveProficiencyRankingService")
    private IAdaptiveProficiencyRankingService iAdaptiveProficiencyRankingService;

    @Autowired
    @Qualifier(CacheEnabledQPalXTutorialService.SPRING_BEAN)
    private IQPalXTutorialService iqPalXTutorialService;

    @Autowired
    @Qualifier(DefaultStudentEnrolmentRecordService.SPRING_BEAN)
    private IStudentEnrolmentRecordService iStudentEnrolmentRecordService;


    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QPalXUserInfoPanelService.class);

    @Override
    public void addUserInfoAttributes(Model model) {
        Assert.notNull(model, "model cannot be null");
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        if(optionalUser.isPresent()) {
            addStudentUserInfoAttributes(model, optionalUser.get());
        }
    }

    @Override
    public void addStudentUserInfoAttributes(Model model, QPalXUser qPalXUser) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(qPalXUser, "qPalXUser cannot be null");

        String userType = qPalXUser.getUserType().toString();
        LOGGER.info("Adding all all QPalX User information panel attributes for user:> {} type:> {}", qPalXUser.getEmail(), userType);
        model.addAttribute(UserInfoPanelAttributesE.LoggedInQPalXUser.toString(), qPalXUser);
        model.addAttribute(UserInfoPanelAttributesE.QPalXUserType.toString(), userType);

        if (qPalXUser.getUserType() == QPalxUserTypeE.STUDENT) {
            buildAndAddAllStudentInfoAttributes(model, qPalXUser);
        }
    }

    private void buildAndAddAllStudentInfoAttributes(Model model, QPalXUser qPalXUser) {
        LOGGER.debug("Building and adding all Student level user information for Student: {}", qPalXUser.getEmail());
        StudentEnrolmentRecord studentEnrolmentRecord = iStudentEnrolmentRecordService.findCurrentStudentEnrolmentRecord(qPalXUser);
        StudentTutorialGrade studentTutorialGrade = studentEnrolmentRecord.getStudentTutorialGrade();
        StudentTutorialLevel studentTutorialLevel = studentTutorialGrade.getStudentTutorialLevel();
        QPalXEducationalInstitution qPalXEducationalInstitution = studentEnrolmentRecord.getEducationalInstitution();

        // Find all the StudentTutorialGrades that are available at this Student's tutorial level.  This will allow the Student to Switch to a new Tutorial Grade higher than their current if they want to
        List<StudentTutorialGrade> allAvailableStudentTutorialGrades = iqPalXTutorialService.findAllStudentTutorialGradeByTutorialLevel(studentTutorialLevel);

        // Add all attributes to model
        model.addAttribute(StudentTutorialLevel.CLASS_ATTRIBUTE_IDENTIFIER, studentTutorialLevel);
        model.addAttribute(StudentTutorialGrade.CLASS_ATTRIBUTE_IDENTIFIER, studentTutorialGrade);
        model.addAttribute(StudentTutorialGrade.CLASS_INSTANCES_IDENTIFIER, allAvailableStudentTutorialGrades);
        model.addAttribute(QPalXEducationalInstitution.CLASS_ATTRIBUTE_IDENTIFIER, qPalXEducationalInstitution);

        // load and all all Student adaptive proficiency rankings
        List<AdaptiveProficiencyRanking> adaptiveProficiencyRankings = iAdaptiveProficiencyRankingService.findStudentAdaptiveProficiencyRankings(qPalXUser);
        model.addAttribute(UserInfoPanelAttributesE.AdpativeProficiencyRankings.toString(), adaptiveProficiencyRankings);
    }

}
