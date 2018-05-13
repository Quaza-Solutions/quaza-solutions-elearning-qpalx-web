package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.GlobalStudentPerformanceAudit;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.StudentEnrolmentRecord;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.CacheEnabledELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.GlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IGlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.DefaultStudentEnrollmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IStudentEnrollmentRecordService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author manyce400
 */
@Service(GuardianUserControlPanelService.BEAN_NAME)
public class GuardianUserControlPanelService implements IGuardianUserControlPanelService {


    @Autowired
    @Qualifier(QPalXUserWebService.BEAN_NAME)
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(QPalXUserInfoPanelService.BEAN_NAME)
    private IQPalXUserInfoPanelService iqPalXUserInfoPanelService;

    @Autowired
    @Qualifier(CacheEnabledELearningCurriculumService.BEAN_NAME)
    private IELearningCurriculumService ieLearningCurriculumService;

    @Autowired
    @Qualifier(DefaultStudentEnrollmentRecordService.SPRING_BEAN)
    private IStudentEnrollmentRecordService iStudentEnrollmentRecordService;

    @Autowired
    @Qualifier(GlobalStudentPerformanceAuditService.SPRING_BEAN)
    private IGlobalStudentPerformanceAuditService iGlobalStudentPerformanceAuditService;


    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.user.GuardianUserControlPanelService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GuardianUserControlPanelService.class);

    @Override
    public void addGuardianUserControlInfo(Model model) {
        Assert.notNull(model, "model cannot be null");
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        if(optionalUser.isPresent() && optionalUser.get().getUserType() == QPalxUserTypeE.PARENT_GUARDIAN) {
            LOGGER.info("Building information required for Guardian user control panel...");

            // Enable displaying of the Student user's information panel
            model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

            // Find enrolmentrecord in order to figure out the core and elective curriculu

            // TODO add support for multiple depdent students for a Guardian
            // Find the student user that this Guardian user is linked to monitor global performance.  Currently only one student per Guardian supported
            List<GlobalStudentPerformanceAudit> globalStudentPerformanceAuditList = iGlobalStudentPerformanceAuditService.findAllGlobalStudentPerformanceAuditForAuditUser(optionalUser.get());
            iqPalXUserInfoPanelService.addStudentUserInfoAttributes(model, globalStudentPerformanceAuditList.get(0).getStudentQPalxUser());
            populateStudentUserCurriculumInfo(model, globalStudentPerformanceAuditList.get(0).getStudentQPalxUser());
        }
    }

    private void populateStudentUserCurriculumInfo(Model model, QPalXUser studentQPalxUser) {
        StudentEnrolmentRecord studentEnrolmentRecord = iStudentEnrollmentRecordService.findCurrentStudentEnrolmentRecord(studentQPalxUser);
        List<ELearningCurriculum> coreELearningCurricula = ieLearningCurriculumService.findAllCurriculumByTutorialGradeAndType(CurriculumType.CORE, studentEnrolmentRecord.getStudentTutorialGrade());
        List<ELearningCurriculum> electiveELearningCurricula = ieLearningCurriculumService.findAllCurriculumByTutorialGradeAndType(CurriculumType.ELECTIVE, studentEnrolmentRecord.getStudentTutorialGrade());
        Map<Integer, List<ELearningCurriculum>> coreCurriculaMap = getCurriculumDisplayMap(coreELearningCurricula);
        Map<Integer, List<ELearningCurriculum>> electiveCurriculaMap = getCurriculumDisplayMap(electiveELearningCurricula);
        model.addAttribute("CoreELearningCurriculaMap", coreCurriculaMap);
        model.addAttribute("ElectiveLearningCurriculaMap", electiveCurriculaMap);
    }

    // TODO move this method
    private Map<Integer, List<ELearningCurriculum>> getCurriculumDisplayMap(List<ELearningCurriculum> coreELearningCurricula) {
        Map<Integer, List<ELearningCurriculum>> displayMap = new HashMap<>();

        int positionCount = 1;
        int keyCount = 1;
        int curriculaCount = coreELearningCurricula.size();

        List<ELearningCurriculum> collector = new ArrayList<>();

        for(ELearningCurriculum eLearningCurriculum: coreELearningCurricula) {
            collector.add(eLearningCurriculum);
            if(positionCount == 3 || keyCount == curriculaCount) {
                displayMap.put(keyCount, new ArrayList<>(collector));
                collector.clear();
                positionCount = 0;
            }

            positionCount++;
            keyCount++;
        }

        return displayMap;
    }

}
