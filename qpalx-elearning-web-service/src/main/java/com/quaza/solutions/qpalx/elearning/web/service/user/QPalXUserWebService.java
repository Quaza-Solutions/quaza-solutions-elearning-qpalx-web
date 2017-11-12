package com.quaza.solutions.qpalx.elearning.web.service.user;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IStudentCurriculumService;
import com.quaza.solutions.qpalx.elearning.web.security.login.WebQPalXUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author manyce400
 */
@Service(QPalXUserWebService.BEAN_NAME)
public class QPalXUserWebService implements IQPalXUserWebService {




    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StudentCurriculumService")
    private IStudentCurriculumService iStudentCurriculumService;



    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QPalXUserWebService.class);


    @Override
    public Optional<QPalXUser> getLoggedInQPalXUser() {
        LOGGER.info("Attempting to retrieve the current logged in QPalX user session");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebQPalXUser webQPalXUser = auth.getPrincipal() instanceof  WebQPalXUser ? (WebQPalXUser) auth.getPrincipal() : null;

        if (webQPalXUser != null) {
            QPalXUser qPalXUser = webQPalXUser.getQPalXUser();
            LOGGER.info("Returning logged in user session for:> {}", qPalXUser.getEmail());
            return Optional.of(qPalXUser);
        }

        return Optional.empty();
    }

    @Override
    public boolean executeAutoLogin(String userEmail) {
        Assert.notNull(userEmail, "userEmail cannot be null");
        LOGGER.info("Attempting to auto-login user: {} to QPalX", userEmail);

        // Executing this call will also validate user subscription details as part of auto loging process
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if(userDetails != null) {
            // Below authentication is the same step SpringBoot Security does authentication so User is good to access web site after this step
            LOGGER.info("Validation process completed succesfully auto-login completed");
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return true;
        }

        LOGGER.warn("Auto-login of user: {} was not succesful", userEmail);
        return false;
    }

    @Override
    public WebQPalXUser getWebQPalXUser() {
        LOGGER.info("Attempting to retrieve the current logged in WebQPalXUser user session");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebQPalXUser webQPalXUser = auth.getPrincipal() instanceof  WebQPalXUser ? (WebQPalXUser) auth.getPrincipal() : null;
        return webQPalXUser;
    }

    @Override
    public void addQPalXUserInfoDetailsToWebModel(Model model, QPalXUser qPalXUser) {
        LOGGER.debug("Adding logged in user details for user: {}", qPalXUser.getEmail());
        model.addAttribute("LoggedInQPalXUser", qPalXUser);
    }

    @Override
    public void addQPalXUserAccesibleCurriculum(Model model, QPalXUser qPalXUser) {
        Assert.notNull(qPalXUser, "qPalxUser cannot be null");

        QPalxUserTypeE qPalxUserTypeE = qPalXUser.getUserType();
        switch (qPalxUserTypeE) {
            case STUDENT:
                break;
            case CONTENT_DEVELOPER:
                break;
            default:
                break;

        }
    }

    private void addContentDeveloperAccessibleCurricula(Model model, QPalXUser qPalXUser) {
        LOGGER.info("Adding accesible elearning curricula for Content Developer user: {}", qPalXUser.getEmail());
//        List<ELearningCurriculum> eLearningCurricula = iStudentCurriculumService.findAllCoreELearningCurriculum();
    }
}
