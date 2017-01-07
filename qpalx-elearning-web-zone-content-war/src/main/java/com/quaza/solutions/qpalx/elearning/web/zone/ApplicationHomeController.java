package com.quaza.solutions.qpalx.elearning.web.zone;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.service.geographical.IGeographicalDateTimeFormatter;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IStudentCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IQPalxUserService;
import com.quaza.solutions.qpalx.elearning.web.service.admin.IContentAdminWebService;
import com.quaza.solutions.qpalx.elearning.web.service.contentpanel.IAdaptiveLearningScoreChartDisplayPanel;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxUserService")
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
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.AdaptiveLearningScoreChartDisplayPanel")
    private IAdaptiveLearningScoreChartDisplayPanel iAdaptiveLearningScoreChartDisplayPanel;

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
                String redirectUrl = "/curriculum-by-tutorialgrade?tutorialGradeID=1&curriculumType=CORE";
                LOGGER.info("Logged in user is a Content Developer, redirecting to:> {}", redirectUrl);
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, redirectUrl);
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
        LOGGER.debug("Returning Ebooks promo patg....");

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());
        model.addAttribute("CurriculumType", "EBooks-Promo");
        iAdaptiveLearningScoreChartDisplayPanel.addEmptyLearningScoreChartDisplayPanel(model);
        return ContentRootE.Student_Home.getContentRootPagePath("ebooks-promo");
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
