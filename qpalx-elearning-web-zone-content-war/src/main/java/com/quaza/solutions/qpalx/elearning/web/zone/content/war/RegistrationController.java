package com.quaza.solutions.qpalx.elearning.web.zone.content.war;

import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXMunicipality;
import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialLevel;
import com.quaza.solutions.qpalx.elearning.service.geographical.IQPalXMunicipalityService;
import com.quaza.solutions.qpalx.elearning.service.institutions.IQPalXEducationalInstitutionService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.RegistrationSelectionWebVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.RegistrationTypeE;
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

import javax.validation.Valid;
import java.util.List;

/**
 * Main controller for handling all QPalX signup request and routing to appropriate Signup processes for
 * Students, Teachers and Parents.
 *
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"RegistrationSelectionWebVO", "QPalXWebUserVO", "QPalXMunicipalities", "StudentTutorialLevels", "StudentTutorialGrades", "QPalXEducationalInstitutions"})
public class RegistrationController {


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXMunicipalityService")
    private IQPalXMunicipalityService iqPalXMunicipalityService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXTutorialService")
    private IQPalXTutorialService iqPalXTutorialService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalXEducationalInstitutionService")
    private IQPalXEducationalInstitutionService iqPalXEducationalInstitutionService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RegistrationController.class);

    @RequestMapping(value = "/qpalx-sign-up", method = RequestMethod.GET)
    public String registrationStartPage(final ModelMap modelMap, Model model) {
        LOGGER.info("Processing QPalX signup request, sending to signup selection page....");
        modelMap.addAttribute("RegistrationSelectionWebVO", new RegistrationSelectionWebVO());
        return ContentRootE.Home.getContentRootPagePath("sign-up");
    }

    @RequestMapping(value = "/sign-up-type-select", method = RequestMethod.POST)
    public String registrationSelectionPage(final ModelMap modelMap, Model model, @Valid @ModelAttribute("RegistrationSelectionWebVO") RegistrationSelectionWebVO registrationSelectionWebVO, BindingResult bindingResult) {
        LOGGER.info("Received initial QPalX Signup selection request registrationSelectionWebVO: {}", registrationSelectionWebVO);


        if(bindingResult.hasErrors()) {
            LOGGER.info("Error encountered in validating Signup selection input, returning to sign-up page...");
            return ContentRootE.Home.getContentRootPagePath("sign-up");
        }

        String selectedSignupTypePage = null;
        RegistrationTypeE registrationTypeE = registrationSelectionWebVO.getAsSignUpTypeE();
        switch (registrationTypeE) {
            case Student:
                LOGGER.info("Student QPalX signup selected, returning student sign-up form");
                selectedSignupTypePage = ContentRootE.Student_Signup.getContentRootPagePath("sign-up-student");
                addRegistrationProcessAttributes(model, modelMap);
                break;
            case Parent:
                LOGGER.info("Parent QPalX signup selected, returning parent sign-up form");
                selectedSignupTypePage = ContentRootE.Guardian_Signup.getContentRootPagePath("sign-up-guardian");
                addRegistrationProcessAttributes(model, modelMap);
                break;
            case Teacher:
                LOGGER.info("Teacher QPalX signup selected which is currently not supported, returning to home page.");
                selectedSignupTypePage = ContentRootE.Home.getContentRootPagePath("launch");
                break;
            default:
                LOGGER.info("Invalid QPalX signup type selected, returning to home page");
                selectedSignupTypePage = ContentRootE.Home.getContentRootPagePath("launch");
                break;
        }

        return selectedSignupTypePage;
    }


    private void addRegistrationProcessAttributes(final Model model, final ModelMap modelMap) {
        List<QPalXMunicipality> municipalities = iqPalXMunicipalityService.findAllQPalXMunicipalities();
        List<StudentTutorialLevel> studentTutorialLevels = iqPalXTutorialService.findAllQPalXTutorialLevels();
        List<StudentTutorialGrade> studentTutorialGrades = iqPalXTutorialService.findAllStudentTutorialGrade();
        List<QPalXEducationalInstitution> qPalXEducationalInstitutions = iqPalXEducationalInstitutionService.findAll();
        modelMap.addAttribute("QPalXWebUserVO", new QPalXWebUserVO());
        modelMap.addAttribute("QPalXMunicipalities", municipalities);
        modelMap.addAttribute("StudentTutorialLevels", studentTutorialLevels);
        modelMap.addAttribute("StudentTutorialGrades", studentTutorialGrades);
        modelMap.addAttribute("QPalXEducationalInstitutions", qPalXEducationalInstitutions);
    }
}
