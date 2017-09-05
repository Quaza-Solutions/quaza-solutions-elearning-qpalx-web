package com.quaza.solutions.qpalx.elearning.web.zone.content.guardian.performance;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.statistics.StudentOverallProgressStatistics;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.GlobalStudentPerformanceAudit;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.statistics.IStudentOverallProgressStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.statistics.StudentOverallProgressStatisticsService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.CacheEnabledELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.GlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IGlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.user.GuardianUserControlPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IGuardianUserControlPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.user.QPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class GuardianGlobalPerformanceController {



    @Autowired
    @Qualifier(QPalXUserWebService.BEAN_NAME)
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(CacheEnabledELearningCurriculumService.BEAN_NAME)
    private IELearningCurriculumService ieLearningCurriculumService;

    @Autowired
    @Qualifier(GuardianUserControlPanelService.BEAN_NAME)
    private IGuardianUserControlPanelService iGuardianUserControlPanelService;

    @Autowired
    @Qualifier(GlobalStudentPerformanceAuditService.BEAN_NAME)
    private IGlobalStudentPerformanceAuditService iGlobalStudentPerformanceAuditService;

    @Autowired
    @Qualifier(StudentOverallProgressStatisticsService.BEAN_NAME)
    private IStudentOverallProgressStatisticsService iStudentOverallProgressStatisticsService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GuardianGlobalPerformanceController.class);


    @RequestMapping(value = "/view-global-curriculum-progress", method = RequestMethod.GET)
    public String accessRegistrationPayment(Model model, @RequestParam(value = "curriculumID") Long curriculumID) {
        LOGGER.info("Building and displaying global curriculum progress for curriculumID: {}", curriculumID);

        ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(curriculumID);

        iGuardianUserControlPanelService.addGuardianUserControlInfo(model);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        QPalXUser guardianUser = optionalUser.get();

        // Find the Overall performance progress report for QPalx Student that this guardian is monitoring
        GlobalStudentPerformanceAudit globalStudentPerformanceAudit = iGlobalStudentPerformanceAuditService.findAllGlobalStudentPerformanceAuditForAuditUser(guardianUser).get(0);
        StudentOverallProgressStatistics studentOverallProgressStatistics = iStudentOverallProgressStatisticsService.getGlobalStudentOverallProgressStatisticsInCurriculum(globalStudentPerformanceAudit.getStudentQPalxUser(), eLearningCurriculum);
        model.addAttribute("CoreProgress", studentOverallProgressStatistics);
        return ContentRootE.Guardian_Global_Performance.getContentRootPagePath("curriculum-overall-progress");
    }


}
