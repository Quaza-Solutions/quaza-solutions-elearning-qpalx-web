package com.quaza.solutions.qpalx.elearning.web.content.teacher;

import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.GlobalStudentPerformanceAudit;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.GlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.IGlobalStudentPerformanceAuditService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.user.QPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class TeacherGlobalAuditController implements ITeacherGlobalAuditController {



    @Autowired
    @Qualifier(QPalXUserWebService.BEAN_NAME)
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(GlobalStudentPerformanceAuditService.SPRING_BEAN)
    private IGlobalStudentPerformanceAuditService iGlobalStudentPerformanceAuditService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TeacherGlobalAuditController.class);


    @Override
    @RequestMapping(value = "/student-curriculum-performance", method = RequestMethod.GET)
    public String findAffiliatedStudentsCurriculumPerfomance(@RequestParam("eLearningCurriculumID") Long eLearningCurriculumID) {
        LOGGER.info("Finding all Student performance in eLearningCurriculumID: {}", eLearningCurriculumID);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        // Find all Student users that are currently mapped to this Teacher
        List<GlobalStudentPerformanceAudit> studentPerformanceAuditList = iGlobalStudentPerformanceAuditService.findAllGlobalStudentPerformanceAuditForAuditUser(optionalUser.get());

        return ContentRootE.School_Teacher_Home.getContentRootPagePath("teacher-dashboard");
    }
}
