package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.GlobalStudentPerformanceAudit;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.GlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IGlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
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
@Service(GuardianUserControlPanelService.BEAN_NAME)
public class GuardianUserControlPanelService implements IGuardianUserControlPanelService {


    @Autowired
    @Qualifier(QPalXUserWebService.BEAN_NAME)
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(QPalXUserInfoPanelService.BEAN_NAME)
    private IQPalXUserInfoPanelService iqPalXUserInfoPanelService;

    @Autowired
    @Qualifier(GlobalStudentPerformanceAuditService.BEAN_NAME)
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

            // Default to CORE ELearning curriculum for global performance control panel
            model.addAttribute("CurriculumType", "CORE");

            // TODO add support for multiple depdent students for a Guardian
            // Find the student user that this Guardian user is linked to monitor global performance.  Currently only one student per Guardian supported
            List<GlobalStudentPerformanceAudit> globalStudentPerformanceAuditList = iGlobalStudentPerformanceAuditService.findAllGlobalStudentPerformanceAuditForAuditUser(optionalUser.get());
            iqPalXUserInfoPanelService.addStudentUserInfoAttributes(model, globalStudentPerformanceAuditList.get(0).getStudentQPalxUser());

        }
    }

}
