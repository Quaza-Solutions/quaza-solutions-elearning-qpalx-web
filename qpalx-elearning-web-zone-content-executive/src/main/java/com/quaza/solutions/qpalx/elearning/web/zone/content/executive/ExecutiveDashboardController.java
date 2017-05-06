package com.quaza.solutions.qpalx.elearning.web.zone.content.executive;

import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author manyce400
 */
@Controller
public class ExecutiveDashboardController {


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExecutiveDashboardController.class);

    @RequestMapping(value = "/Executive-Top-Performing-Schools", method = RequestMethod.GET)
    public String viewAdminQPalXLessons(final Model model) {
        LOGGER.info("[Executive-Dasboards] - processing request for top performing schools...");
        return ContentRootE.Executive_User_Home.getContentRootPagePath("top-performing-schools");
    }
}
