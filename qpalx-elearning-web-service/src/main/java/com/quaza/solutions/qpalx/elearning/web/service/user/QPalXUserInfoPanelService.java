package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.AdaptiveProficiencyRanking;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.StudentEnrolmentRecord;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.IAdaptiveProficiencyRankingService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.DefaultStudentEnrolmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IStudentEnrolmentRecordService;
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
    @Qualifier(DefaultStudentEnrolmentRecordService.SPRING_BEAN)
    private IStudentEnrolmentRecordService iStudentEnrolmentRecordService;


    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QPalXUserInfoPanelService.class);

    @Override
    public void addUserInfoAttributes(Model model) {
        Assert.notNull(model, "model cannot be null");
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        if(optionalUser.isPresent()) {
            QPalXUser qPalXUser = optionalUser.get();
            String userType = qPalXUser.getUserType().toString();
            LOGGER.info("Adding all all QPalx User information panel attributes for user:> {} type:> {}", qPalXUser.getEmail(), userType);
            model.addAttribute(UserInfoPanelAttributesE.LoggedInQPalXUser.toString(), qPalXUser);
            model.addAttribute(UserInfoPanelAttributesE.QPalXUserType.toString(), userType);

            // Find user Enrolment record and add attributes used for displaying Student info in panel
            StudentEnrolmentRecord studentEnrolmentRecord = iStudentEnrolmentRecordService.findCurrentStudentEnrolmentRecord(qPalXUser);
            LOGGER.debug("Found studentEnrolmentRecord: {}", studentEnrolmentRecord);
            model.addAttribute(StudentTutorialGrade.CLASS_ATTRIBUTE_IDENTIFIER, studentEnrolmentRecord.getStudentTutorialGrade());
            model.addAttribute(QPalXEducationalInstitution.CLASS_ATTRIBUTE_IDENTIFIER, studentEnrolmentRecord.getEducationalInstitution());

            // load and all all Student adaptive proficiency rankings
            List<AdaptiveProficiencyRanking> adaptiveProficiencyRankings = iAdaptiveProficiencyRankingService.findStudentAdaptiveProficiencyRankings(qPalXUser);
            model.addAttribute(UserInfoPanelAttributesE.AdpativeProficiencyRankings.toString(), adaptiveProficiencyRankings);
        }
    }

}
