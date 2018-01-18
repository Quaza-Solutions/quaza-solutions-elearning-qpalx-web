package com.quaza.solutions.qpalx.elearning.web.zone.content.student.registration;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.EnrollmentDecision;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.DefaultStudentEnrollmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IStudentEnrollmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.CacheEnabledQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.WebOperationErrorAttributesE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.DefaultRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class StudentEnrollmentController {




    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(CacheEnabledQPalXTutorialService.SPRING_BEAN)
    private IQPalXTutorialService iqPalXTutorialService;

    @Autowired
    @Qualifier(DefaultStudentEnrollmentRecordService.SPRING_BEAN)
    private IStudentEnrollmentRecordService iStudentEnrollmentRecordService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier(DefaultRedirectStrategyExecutor.BEAN_NAME)
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentEnrollmentController.class);


    @RequestMapping(value = "/enroll-for-tutorial-grade", method = RequestMethod.POST)
    public void enrollStudentForTutorialGrade(@ModelAttribute("QPalXWebUserVO") QPalXWebUserVO qPalXWebUserVO,
                                              ModelMap modelMap, Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        LOGGER.info("Attempting to enroll Student in a new TutorialGrade with params: {}", qPalXWebUserVO);

        StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(qPalXWebUserVO.getTutorialGradeID());
        EnrollmentDecision enrollmentDecision = iStudentEnrollmentRecordService.enrollStudentTutorialGrade(optionalUser.get(), studentTutorialGrade);
        if (enrollmentDecision.isEnrollmentDenied()) {
            LOGGER.info("Student enrollment request has been denied, routing to account info...");
            iRedirectStrategyExecutor.sendRedirectWithObject("/account-info", enrollmentDecision, WebOperationErrorAttributesE.Invalid_Upgrade_Operation, httpServletRequest, httpServletResponse);
        } else {
            LOGGER.info("Student enrollment request has approved, routing to welcome page...");
            iRedirectStrategyExecutor.sendRedirect(httpServletRequest, httpServletResponse, "/enrollment-completion-notice");
        }
    }

    @RequestMapping(value = "/enrollment-completion-notice", method = RequestMethod.GET)
    public String enrollStudentForTutorialGrade(Model model) {
        LOGGER.info("Routing to new enrollment page");

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        return ContentRootE.Student_Home.getContentRootPagePath("enrollment-success");
    }
}
