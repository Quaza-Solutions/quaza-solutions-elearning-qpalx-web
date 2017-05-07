package com.quaza.solutions.qpalx.elearning.web.zone.content.student.dashboard;

import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author manyce400
 */
@Controller
public class StudentDashboardController {



    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StudentDashboardController.class);


    @RequestMapping(value = "/VIEW_STUDENT_DASHBOARD", method = RequestMethod.GET)
    public String viewAdminQPalXLessons(final Model model) {
        LOGGER.info("[Student-Dasboards] - processing request for main Student dashboard page...");
        return ContentRootE.Student_Home.getContentRootPagePath("dashboard");
    }
}
