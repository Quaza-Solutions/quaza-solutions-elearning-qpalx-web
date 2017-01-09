package com.quaza.solutions.qpalx.elearning.web.zone.content.student.account;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.statistics.StudentOverallProgressStatistics;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.statistics.IStudentOverallProgressStatisticsService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.LessonsAdminAttributesE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class OverallProgressController {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StudentOverallProgressStatisticsService")
    private IStudentOverallProgressStatisticsService iStudentOverallProgressStatisticsService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(OverallProgressController.class);


    @RequestMapping(value = "/overall-student-progress", method = RequestMethod.GET)
    public String viewOverallStudentProgress(final Model model) {
        LOGGER.info("Retrieving and displaying overall Student progress page....");

        // Add display attributes for Student information panel.
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        // Load up all progress statistics in the CORE and ELECTIVE Curriculums
        List<StudentOverallProgressStatistics> coreOverallProgressStatistics = iStudentOverallProgressStatisticsService.getStudentOverallProgressStatistics(optionalUser.get(), CurriculumType.CORE);
        model.addAttribute(LessonsAdminAttributesE.CoreCurriculumOverallProgress.toString(), coreOverallProgressStatistics);

        List<StudentOverallProgressStatistics> electiveOverallProgressStatistics = iStudentOverallProgressStatisticsService.getStudentOverallProgressStatistics(optionalUser.get(), CurriculumType.ELECTIVE);
        model.addAttribute(LessonsAdminAttributesE.ElectiveCurriculumOverallProgress.toString(), electiveOverallProgressStatistics);

        return ContentRootE.Student_Adaptive_Learning.getContentRootPagePath("overall-progress-display");
    }


}
