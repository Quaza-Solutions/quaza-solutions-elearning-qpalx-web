package com.quaza.solutions.qpalx.elearning.web.zone.content.student.registration;

import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXMunicipality;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.subjectmatter.proficiency.SimplifiedProficiencyRankE;
import com.quaza.solutions.qpalx.elearning.domain.subscription.QPalXSubscription;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.service.geographical.IGeographicalDateTimeFormatter;
import com.quaza.solutions.qpalx.elearning.service.geographical.IQPalXMunicipalityService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.CacheEnabledELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.prepaidsubscription.IQPalxPrepaidIDService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.DefaultQPalxUserService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IQPalXUserSubscriptionService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IQPalxUserService;
import com.quaza.solutions.qpalx.elearning.service.subscription.IQPalxSubscriptionService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.student.StudentSignUpModelAttributeE;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.*;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"RegistrationSelectionWebVO", "QPalXWebUserVO", "QPalXMunicipalities", "StudentTutorialLevels", "StudentTutorialGrades", "QPalXEducationalInstitutions"})
public class StudentRegistrationController {




    @Autowired
    @Qualifier(DefaultQPalxUserService.BEAN_NAME)
    private IQPalxUserService iqPalxUserService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultGeographicalDateTimeFormatter")
    private IGeographicalDateTimeFormatter iGeographicalDateTimeFormatter;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXMunicipalityService")
    private IQPalXMunicipalityService iqPalXMunicipalityService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxSubscriptionService")
    private IQPalxSubscriptionService iqPalxSubscriptionService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalXUserSubscriptionService")
    private IQPalXUserSubscriptionService iqPalXUserSubscriptionService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXTutorialService")
    private IQPalXTutorialService iqPalXTutorialService;

    @Autowired
    @Qualifier(CacheEnabledELearningCurriculumService.BEAN_NAME)
    private IELearningCurriculumService ieLearningCurriculumService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxPrepaidIDService")
    private IQPalxPrepaidIDService iQpalxPrepaidIDService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentRegistrationController.class);


    @RequestMapping(value = "/select-signup-payment", method = RequestMethod.POST)
    public String accessRegistrationPayment(Model model,  @Valid @ModelAttribute("QPalXWebUserVO") QPalXWebUserVO qPalXWebUserVO, BindingResult bindingResult) {
        LOGGER.info("Student signup payment requested with qPalXWebUserVO: {}", qPalXWebUserVO);

        if(bindingResult.hasErrors()) {
            LOGGER.info("Error encountered in validating qPalXWebUserVO returning to Student information collection page...");
            return ContentRootE.Student_Signup.getContentRootPagePath("sign-up-student");
        }

        // Verify that the email address entered does not exist already for a different account
        QPalXUser existingUser = iqPalxUserService.findQPalXUserByEmail(qPalXWebUserVO.getEmail());
        if(existingUser != null) {
            LOGGER.info("Duplicate User has been detected for email provided, alerting user for re-entry.");
            model.addAttribute("StudentSignupDomainError", "* Existing Account found for email address entered. Enter a new email");
            return ContentRootE.Student_Signup.getContentRootPagePath("sign-up-student");
        }

        // Verify that the mobile phone number user has provided is unique.  All phone numbers have to be unique
        boolean uniquePhoneNumber = iqPalxUserService.isUniqueUserMobilePhoneNumber(qPalXWebUserVO.getMobilePhoneNumber());
        if(!uniquePhoneNumber) {
            LOGGER.info("Duplicate mobile phone number detected, alerting user for re-entry.");
            model.addAttribute("StudentSignupDomainError", "* Existing Account found for phone number entered. Enter a new number");
            return ContentRootE.Student_Signup.getContentRootPagePath("sign-up-student");
        }

        StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(qPalXWebUserVO.getTutorialGradeID());
        qPalXWebUserVO.setStudentTutorialLevelID(studentTutorialGrade.getStudentTutorialLevel().getId());

        // Find all currently available prepaid subscriptions for Ghana.  Default for now.
        // TODO replace with student's home country
        List<QPalXSubscription> qPalXSubscriptionList = iqPalxSubscriptionService.findAllQPalXSubscriptionsByCountryCode("GH");
        model.addAttribute(StudentSignUpModelAttributeE.AvailableSubscriptions.toString(), qPalXSubscriptionList);
        return ContentRootE.Student_Signup.getContentRootPagePath("payment-selection");
    }

    @RequestMapping(value = "/customize-proficiency-ranking", method = RequestMethod.POST)
    public String customizeStudentProficiencyRankings(final ModelMap modelMap, Model model, @ModelAttribute("QPalXWebUserVO") QPalXWebUserVO qPalXWebUserVO) {
        LOGGER.info("Processing student signup payment page with qPalXWebUserVO: {}", qPalXWebUserVO);
        model.addAttribute("SimplifiedProficiencyRanks", SimplifiedProficiencyRankE.values());

        LOGGER.info("Attempting to redeem prepaid code: {} ..", qPalXWebUserVO.getPrepaidValue());

        // Redeem prepaid code using the selected municipality
        QPalXMunicipality qPalXMunicipality = iqPalXMunicipalityService.findQPalXMunicipalityByID(qPalXWebUserVO.getMunicipalityID());

        // Find all Content both Core and Elective for the TutorialGrade that the Student selected
        StudentTutorialGrade studentTutorialGrade = iqPalXTutorialService.findTutorialGradeByID(qPalXWebUserVO.getTutorialGradeID());
        List<ELearningCurriculum> coreELearningCurricula = ieLearningCurriculumService.findAllCurriculumByTutorialGradeAndType(CurriculumType.CORE, studentTutorialGrade);
        List<ELearningCurriculum> electiveELearningCurricula = ieLearningCurriculumService.findAllCurriculumByTutorialGradeAndType(CurriculumType.ELECTIVE, studentTutorialGrade);

        Map<Integer, List<ELearningCurriculum>> coreCurriculaMap = getCurriculumDisplayMap(coreELearningCurricula);
        Map<Integer, List<ELearningCurriculum>> electiveCurriculaMap = getCurriculumDisplayMap(electiveELearningCurricula);

        model.addAttribute("CoreELearningCurriculaMap", coreCurriculaMap);
        model.addAttribute("ElectiveLearningCurriculaMap", electiveCurriculaMap);


        if(qPalXWebUserVO.getIncorrectValueCounter() == 5){
            return "/launch.html";//penalty
        }
        else {
            // Attempt to redeem code
            boolean codeRedeemed = iQpalxPrepaidIDService.redeemCode(qPalXWebUserVO.getPrepaidValue(), qPalXWebUserVO.getSubscriptionID(), qPalXMunicipality);

            if (codeRedeemed) {
                qPalXWebUserVO.setRedemptionFailure(false);
                model.addAttribute("QPalXWebUserVO", qPalXWebUserVO);
                LOGGER.info("Processing student signup payment page with qPalXWebUserVO: {}", qPalXWebUserVO);
                model.addAttribute("SimplifiedProficiencyRanks", SimplifiedProficiencyRankE.values());
                return ContentRootE.Student_Signup.getContentRootPagePath("proficiency");
            } else {
                qPalXWebUserVO.setRedemptionFailure(true);
                int holder = qPalXWebUserVO.getIncorrectValueCounter();
                holder++;
                qPalXWebUserVO.setIncorrectValueCounter(holder);
                model.addAttribute("QPalXWebUserVO", qPalXWebUserVO);
                return ContentRootE.Student_Signup.getContentRootPagePath("payment-selection");
            }
        }
    }

    @RequestMapping(value = "/complete-qpalx-signup", method = RequestMethod.POST)
    public String completeQPalXSignup(final SessionStatus status, @ModelAttribute("QPalXWebUserVO") QPalXWebUserVO qPalXWebUserVO) {
        LOGGER.info("Attempting to create new QPalX subscription using qPalXWebUserVO: {} ", qPalXWebUserVO);


        // save all subscription details
        Optional<QPalXUser> optionalQPalXUser = iqPalXUserSubscriptionService.createNewQPalXUserWithTutorialSubscription(qPalXWebUserVO);

        // Update the redemption code with newly created user that redeemed it
        iQpalxPrepaidIDService.updateRedemptionDetails(qPalXWebUserVO.getPrepaidValue(), optionalQPalXUser.get());

        status.isComplete();

        if(optionalQPalXUser.isPresent()) {
            LOGGER.info("QPalXUser subscription has been succesfully processed, returning to QPalX home page to signup...");
            return ContentRootE.Home.getContentRootPagePath("homepage");
        }

        return ContentRootE.Home.getContentRootPagePath("homepage");
    }


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
