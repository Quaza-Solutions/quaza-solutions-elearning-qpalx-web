package com.quaza.solutions.qpalx.elearning.web.content.teacher.registration;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.DefaultQPalXUserSubscriptionService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IQPalXUserSubscriptionService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.user.QPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.DefaultRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IRedirectStrategyExecutor;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
public class TeacherRegistrationController {



    @Autowired
    @Qualifier(QPalXUserWebService.BEAN_NAME)
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(DefaultQPalXUserSubscriptionService.BEAN_NAME)
    private IQPalXUserSubscriptionService iqPalXUserSubscriptionService;


    @Autowired
    @Qualifier(DefaultRedirectStrategyExecutor.BEAN_NAME)
    private IRedirectStrategyExecutor iRedirectStrategyExecutor;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TeacherRegistrationController.class);

    @RequestMapping(value = "/complete-teacher-registration", method = RequestMethod.POST)
    public void completeTeacherRegistration(@ModelAttribute(QPalXWebUserVO.CLASS_ATTRIBUTE) QPalXWebUserVO qPalXWebUserVO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BindingResult bindingResult) {
        LOGGER.info("Validating and completing QPalX teacher sign-up request...");

        if(bindingResult.hasErrors()) {
            LOGGER.info("Error encountered in validating qPalXWebUserVO returning to Student information collection page...");
            //return ContentRootE.Student_Signup.getContentRootPagePath("sign-up-student");
        }

        Optional<QPalXUser> teacherUser = iqPalXUserSubscriptionService.createNewQPalXUser(qPalXWebUserVO, QPalxUserTypeE.Teacher);

        if(teacherUser.isPresent()) {
            LOGGER.info("QPalXUser Teacher user account has been succesfully created, executing auto login...");
            boolean autoLoggedIn = iqPalXUserWebService.executeAutoLogin(teacherUser.get().getEmail());
        }

        iRedirectStrategyExecutor.sendRedirect(httpServletRequest, httpServletResponse, "/");
    }
}
