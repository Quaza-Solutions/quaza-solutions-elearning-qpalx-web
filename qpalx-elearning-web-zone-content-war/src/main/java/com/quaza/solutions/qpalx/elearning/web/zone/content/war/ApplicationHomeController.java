package com.quaza.solutions.qpalx.elearning.web.zone.content.war;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.service.geographical.IGeographicalDateTimeFormatter;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IStudentCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.DefaultQPalxUserService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IQPalxUserService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.DefaultContentAdminProfileRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IContentAdminProfileRecordService;
import com.quaza.solutions.qpalx.elearning.service.subscription.IQPalxSubscriptionService;
import com.quaza.solutions.qpalx.elearning.web.security.login.UserAuthFailureE;
import com.quaza.solutions.qpalx.elearning.web.security.login.WebQPalXUser;
import com.quaza.solutions.qpalx.elearning.web.service.admin.IContentAdminWebService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.AcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAcademicLevelAdminPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAdaptiveLearningScoreChartDisplayPanel;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.PlatformAdminManagementModeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.GuardianUserControlPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IGuardianUserControlPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Main Controller that handles all access to main QPalX home and login pages.  User authentication is
 * performed along with Spring Security to make sure that a Login is presented if user are not properly
 * authenticated.
 *
 * @author manyce400
 */
@Controller
public class ApplicationHomeController {



    @Autowired
    @Qualifier(DefaultQPalxUserService.BEAN_NAME)
    private IQPalxUserService iqPalxUserService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StudentCurriculumService")
    private IStudentCurriculumService iStudentCurriculumService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultGeographicalDateTimeFormatter")
    private IGeographicalDateTimeFormatter iGeographicalDateTimeFormatter;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.ContentAdminWebService")
    private IContentAdminWebService iContentAdminWebService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxSubscriptionService")
    private IQPalxSubscriptionService iqPalxSubscriptionService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.AdaptiveLearningScoreChartDisplayPanel")
    private IAdaptiveLearningScoreChartDisplayPanel iAdaptiveLearningScoreChartDisplayPanel;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledELearningCurriculumService")
    private IELearningCurriculumService ieLearningCurriculumService;

    @Autowired
    @Qualifier(DefaultContentAdminProfileRecordService.SPRING_BEAN)
    private IContentAdminProfileRecordService iContentAdminProfileRecordService;

    @Autowired
    @Qualifier(GuardianUserControlPanelService.BEAN_NAME)
    private IGuardianUserControlPanelService iGuardianUserControlPanelService;

    @Autowired
    @Qualifier(AcademicLevelAdminPanelService.BEAN_NAME)
    private IAcademicLevelAdminPanelService iAcademicLevelAdminPanelService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApplicationHomeController.class);


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String execQPalxMainHomePage(final Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        LOGGER.info("Received request to access main QPalx home page...");
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        if(optionalUser.isPresent()) {
            LOGGER.info("Current user logged in with email:> {}", optionalUser.get().getEmail());

            if (QPalxUserTypeE.STUDENT == optionalUser.get().getUserType()) {
                // Add all attributes required for User information panel
                qPalXUserInfoPanelService.addUserInfoAttributes(model);
                model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());
                addQPalXUserDetailsToResponse(model, CurriculumType.CORE, optionalUser.get());
                iAdaptiveLearningScoreChartDisplayPanel.addEmptyLearningScoreChartDisplayPanel(model);
                return ContentRootE.Student_Home.getContentRootPagePath("homepage");
            } else if(QPalxUserTypeE.CONTENT_DEVELOPER == optionalUser.get().getUserType()) {
                StudentTutorialGrade studentTutorialGrade = iAcademicLevelAdminPanelService.findBaseAdminAssignedStudentTutorialGrade();
                LOGGER.info("Found Content_Developer Base studentTutorialGrade : {}", studentTutorialGrade.getTutorialGrade());
                String redirectUrl = "/curriculum-by-tutorialgrade?tutorialGradeID=" + studentTutorialGrade.getId() +"&curriculumType=CORE";
                LOGGER.info("Logged in user is a Content Developer, redirecting to:> {}", redirectUrl);
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);
            } else if(QPalxUserTypeE.Executive == optionalUser.get().getUserType()) {
                LOGGER.info("Logged in user is an executive, directing to homepage...");
                return ContentRootE.Executive_User_Home.getContentRootPagePath("homepage");
            } else if(QPalxUserTypeE.Principal == optionalUser.get().getUserType()) {
                LOGGER.info("Logged in user is a Principal, directing to homepage...");
                return ContentRootE.School_Principal_Home.getContentRootPagePath("homepage");
            } else if(QPalxUserTypeE.Teacher == optionalUser.get().getUserType()) {
                LOGGER.info("Logged in user is a Teacher, directing to homepage...");
                return ContentRootE.School_Teacher_Home.getContentRootPagePath("homepage");
            } else if(QPalxUserTypeE.PARENT_GUARDIAN == optionalUser.get().getUserType()) {
                // Default to CORE ELearning curriculum for initial Guardian hompage view
                model.addAttribute("CurriculumType", "CORE");

                iGuardianUserControlPanelService.addGuardianUserControlInfo(model);
                LOGGER.info("Logged in user is a Parent, directing to homepage...");
                return ContentRootE.Guardian_Home.getContentRootPagePath("homepage");
            } else if(QPalxUserTypeE.PLATFORM_ADMIN == optionalUser.get().getUserType()) {
                LOGGER.info("Logged in User is a PLATFORM_ADMIN forwarding to PLATFORM_ADMIN home");

                // Enable the Platform administration mode
                model.addAttribute("PlatformAdminManagementModeE", PlatformAdminManagementModeE.SubscriptionManagementMode.toString());
                return ContentRootE.Platform_Admin_Home.getContentRootPagePath("platform_admin_home");
            }

            LOGGER.info("Only Student QPalX users currently supported");
            return ContentRootE.Home.getContentRootPagePath("homepage");
        } else {
            LOGGER.info("Valid logged in QPalxUser session not found, redirecting to main home page.");
            return ContentRootE.Home.getContentRootPagePath("homepage");
        }
    }

    @RequestMapping(value = "/select-curriculum", method = RequestMethod.GET)
    public String execQPalxMainHomePageWithCurriculumType(final Model model, @RequestParam("curricumlumType") String curricumlumType) {
        LOGGER.info("Received request to access main QPalx home page with curricumlumType:> {}", curricumlumType);
        CurriculumType curriculumType = CurriculumType.valueOf(curricumlumType);
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        if(optionalUser.isPresent() && curriculumType != null) {
            LOGGER.info("Current user logged in with email:> {}", optionalUser.get().getEmail());

            if (QPalxUserTypeE.STUDENT == optionalUser.get().getUserType()) {
                // Add all attributes required for User information panel
                qPalXUserInfoPanelService.addUserInfoAttributes(model);
                model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

                addQPalXUserDetailsToResponse(model, curriculumType, optionalUser.get());
                iAdaptiveLearningScoreChartDisplayPanel.addEmptyLearningScoreChartDisplayPanel(model);
                return ContentRootE.Student_Home.getContentRootPagePath("homepage");
            }

            LOGGER.info("Only Student QPalX users currently supported");
            return ContentRootE.Home.getContentRootPagePath("homepage");
        } else {
            LOGGER.info("Valid logged in QPalxUser session not found, redirecting to main home page.");
            return ContentRootE.Home.getContentRootPagePath("homepage");
        }
    }

    @RequestMapping(value = "/ebooks-promo", method = RequestMethod.GET)
    public String dispalyEBooksPromo(final Model model) {
        LOGGER.debug("Returning Ebooks promo page....");

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());
        model.addAttribute("CurriculumType", "EBooks-Promo");
        iAdaptiveLearningScoreChartDisplayPanel.addEmptyLearningScoreChartDisplayPanel(model);
        return ContentRootE.Student_Home.getContentRootPagePath("ebooks-promo");
    }


    @RequestMapping(value = "/qpalx-access-failure", method = {RequestMethod.GET, RequestMethod.POST})
    public String indexMain(ModelMap modelMap, Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        WebQPalXUser validationFailureUser = (WebQPalXUser) httpServletRequest.getSession().getAttribute("AuthenticationFailedUser");
        UserAuthFailureE userAuthFailureE =  (UserAuthFailureE) httpServletRequest.getSession().getAttribute("UserAuthFailureE");

        if (validationFailureUser != null
                && validationFailureUser.hasValidQPalXUser()
                && userAuthFailureE == UserAuthFailureE.Subscription_Expired) {
            LOGGER.info("Detected that User Subscription has expired, allowing Student user to purchase a new subscription...");
            QPalXUser qPalXUser = validationFailureUser.getQPalXUser();

            //build from the validation failure user
            QPalXWebUserVO qPalXWebUserVO = QPalXWebUserVO.builder()
                    .firstName(qPalXUser.getFirstName())
                    .lastName(qPalXUser.getLastName())
                    .email(qPalXUser.getEmail())
                    .password(qPalXUser.getPassword())
                    .municipalityID(qPalXUser.getQPalXMunicipality().getId())
                    .build();

            // clean up session
//            httpServletRequest.getSession().removeAttribute("AuthenticationFailedUser");
//            httpServletRequest.getSession().removeAttribute("UserAuthFailureE");

            modelMap.addAttribute("SubscriptionRenewQPalXWebUserVO", qPalXWebUserVO);

            return ContentRootE.Student_Signup.getContentRootPagePath("subscription_renewal_payment");
        } else {
            LOGGER.info("Invalid Qpalx User login detected, redirecting to home page...");
            model.addAttribute("CredentialsAuthentication", "Invalid Login Detected");
            return ContentRootE.Home.getContentRootPagePath("homepage");
        }
    }

    private void addQPalXUserDetailsToResponse(final Model model, CurriculumType curriculumType, QPalXUser qPalXUser) {
        List<ELearningCurriculum> eLearningCurricula = null;
        switch (curriculumType) {
            case CORE:
                eLearningCurricula = iStudentCurriculumService.findAllStudentCoreELearningCurriculum(qPalXUser);
                break;
            case ELECTIVE:
                eLearningCurricula = iStudentCurriculumService.findAllStudentElectiveELearningCurriculum(qPalXUser);
                break;
            default:
                break;

        }

        model.addAttribute("CurriculumType", curriculumType.toString());
        //model.addAttribute("LoggedInQPalXUser", qPalXUser);
        model.addAttribute("StudentUserCurricula", eLearningCurricula);

    }


}
