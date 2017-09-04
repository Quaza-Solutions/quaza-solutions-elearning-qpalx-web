package com.quaza.solutions.qpalx.elearning.web.zone.content.guardian.registration;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.StudentEnrolmentRecord;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.DefaultQPalxUserService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IQPalxUserService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.DefaultStudentEnrolmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IStudentEnrolmentRecordService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"RegistrationSelectionWebVO", "QPalXWebUserVO"})
public class GuardianRegistrationController {



    @Autowired
    @Qualifier(DefaultQPalxUserService.BEAN_NAME)
    private IQPalxUserService iqPalxUserService;

    @Autowired
    @Qualifier(DefaultStudentEnrolmentRecordService.SPRING_BEAN)
    private IStudentEnrolmentRecordService iStudentEnrolmentRecordService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GuardianRegistrationController.class);



    @RequestMapping(value = "/link-guardian-student", method = RequestMethod.POST)
    public String accessRegistrationPayment(Model model, @Valid @ModelAttribute("QPalXWebUserVO") QPalXWebUserVO qPalXWebUserVO, @RequestParam(value = "successID") String successID) {
        LOGGER.info("Generating Guardian link to QPalX student page for student with successID: {}", successID);


        // IF SuccessID is not provided send to Form to allow guardian to enter a Student SuccessID
        if(successID == null || successID.trim().length() == 0) {
            return ContentRootE.Guardian_Signup.getContentRootPagePath("link-guardian-student");
        } else {
            QPalXUser dependentStudent = iqPalxUserService.findQPalXUserBySuccessID(successID);

            if(dependentStudent == null) {
                LOGGER.info("No dependent student found for successID: {} redirecting to Guardian retry", successID);
                model.addAttribute("ValidationError", "No Student found in our system with SuccessID: "+successID);
                return ContentRootE.Guardian_Signup.getContentRootPagePath("link-guardian-student");
            }

            // Set the student user that will have their performance globally monitored by this guardian user
            qPalXWebUserVO.setGlobalAuditQPalxUser(dependentStudent);

            // Find the enrollment record for this student
            StudentEnrolmentRecord studentEnrolmentRecord = iStudentEnrolmentRecordService.findCurrentStudentEnrolmentRecord(dependentStudent);

            LOGGER.info("Dependent student has been identified redirecting to Guardian confirmation page");
            model.addAttribute("QPalXUser", dependentStudent);
            model.addAttribute("StudentTutorialGrade", studentEnrolmentRecord.getStudentTutorialGrade());
            model.addAttribute("QPalXEducationalInstitution", studentEnrolmentRecord.getEducationalInstitution());
            return ContentRootE.Guardian_Signup.getContentRootPagePath("sign-up-guardian-confirmation");
        }

    }

    @RequestMapping(value = "/complete-guardian-registration", method = RequestMethod.POST)
    public String completeGuardianRegistration(@Valid @ModelAttribute("QPalXWebUserVO") QPalXWebUserVO qPalXWebUserVO, SessionStatus status) {
        LOGGER.info("Completing guardian signup registration for qPalXWebUserVO: {}", qPalXWebUserVO);
        iqPalxUserService.buildAndSaveGuardianUser(qPalXWebUserVO);
        status.isComplete();
        return ContentRootE.Home.getContentRootPagePath("homepage");
    }


}
