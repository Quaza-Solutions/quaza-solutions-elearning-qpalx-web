package com.quaza.solutions.qpalx.elearning.web.zone.content.guardian.registration;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.StudentEnrolmentRecord;
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

import javax.validation.Valid;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"RegistrationSelectionWebVO", "QPalXWebUserVO"})
public class GuardianRegistrationController {



    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxUserService")
    private IQPalxUserService iqPalxUserService;


    @Autowired
    @Qualifier(DefaultStudentEnrolmentRecordService.SPRING_BEAN)
    private IStudentEnrolmentRecordService iStudentEnrolmentRecordService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GuardianRegistrationController.class);


    // Allows the gardian user thats registering to link to their child/ward that they would like to track on QPalX
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

            // Find the enrollment record for this student
            StudentEnrolmentRecord studentEnrolmentRecord = iStudentEnrolmentRecordService.findCurrentStudentEnrolmentRecord(dependentStudent);

            LOGGER.info("Dependent student has been identified redirecting to Guardian confirmation page");
            model.addAttribute("QPalXUser", dependentStudent);
            model.addAttribute("StudentTutorialGrade", studentEnrolmentRecord.getStudentTutorialGrade());
            model.addAttribute("QPalXEducationalInstitution", studentEnrolmentRecord.getEducationalInstitution());
            return ContentRootE.Guardian_Signup.getContentRootPagePath("sign-up-guardian-confirmation");
        }



        // Verify that the email address entered does not exist already for a different account
//        QPalXUser existingUser = iqPalxUserService.findQPalXUserByEmail(qPalXWebUserVO.getEmail());
//        if(existingUser != null) {
//            LOGGER.info("Duplicate User has been detected for email provided, alerting user for re-entry.");
//            model.addAttribute("StudentSignupDomainError", "* Existing Account found for email address entered. Enter a new email");
//            return ContentRootE.Student_Signup.getContentRootPagePath("sign-up-student");
//        }
//
//        // Verify that the mobile phone number user has provided is unique.  All phone numbers have to be unique
//        boolean uniquePhoneNumber = iqPalxUserService.isUniqueUserMobilePhoneNumber(qPalXWebUserVO.getMobilePhoneNumber());
//        if(!uniquePhoneNumber) {
//            LOGGER.info("Duplicate mobile phone number detected, alerting user for re-entry.");
//            model.addAttribute("StudentSignupDomainError", "* Existing Account found for phone number entered. Enter a new number");
//            return ContentRootE.Student_Signup.getContentRootPagePath("sign-up-student");
//        }
//
//        StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(qPalXWebUserVO.getTutorialGradeID());
//        qPalXWebUserVO.setStudentTutorialLevelID(studentTutorialGrade.getStudentTutorialLevel().getId());
//
//        // Find all currently available prepaid subscriptions for Ghana.  Default for now.
//        // TODO replace with student's home country
//        List<QPalXSubscription> qPalXSubscriptionList = iqPalxSubscriptionService.findAllQPalXSubscriptionsByCountryCode("GH");
//        model.addAttribute(StudentSignUpModelAttributeE.AvailableSubscriptions.toString(), qPalXSubscriptionList);
//        return ContentRootE.Student_Signup.getContentRootPagePath("payment-selection");
    }


}
