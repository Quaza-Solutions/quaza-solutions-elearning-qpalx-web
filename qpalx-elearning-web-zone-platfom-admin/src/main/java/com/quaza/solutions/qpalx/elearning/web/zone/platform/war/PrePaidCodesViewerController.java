package com.quaza.solutions.qpalx.elearning.web.zone.platform.war;

import com.quaza.solutions.qpalx.elearning.domain.subscription.SubscriptionCodeBatchSession;
import com.quaza.solutions.qpalx.elearning.service.prepaidsubscription.IQPalxPrepaidIDService;
import com.quaza.solutions.qpalx.elearning.service.subscription.ISubscriptionCodeBatchSessionService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.PlatformAdminManagementModeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.SubscriptionManagementAttributeE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author manyce400
 */
@Controller
public class PrePaidCodesViewerController {


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxPrepaidIDService")
    private IQPalxPrepaidIDService iQpalxPrepaidIDService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.SubscriptionCodeBatchSessionService")
    private ISubscriptionCodeBatchSessionService iSubscriptionCodeBatchSessionService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PrePaidCodesViewerController.class);


    @PreAuthorize("hasAuthority('PLATFORM_ADMIN')")
    @RequestMapping(value = "/view-open-subcription-code-batches", method = RequestMethod.GET)
    public String accessOpenSubscriptionBatchesView(Model model) {
        LOGGER.info("Retrieving all currently open subscription code batches....");

        // Enable the Platform administration mode
        model.addAttribute("PlatformAdminManagementModeE", PlatformAdminManagementModeE.SubscriptionManagementMode.toString());

        // Find all open subscription batches
        List<SubscriptionCodeBatchSession> openSubscriptionCodeBatchSessions = iSubscriptionCodeBatchSessionService.findAllOpenSubscriptionBatches();
        model.addAttribute(SubscriptionManagementAttributeE.OpenSubscriptionCodeBatchSessions.toString(), openSubscriptionCodeBatchSessions);

        return ContentRootE.Platform_Admin_Prepaid_Codes.getContentRootPagePath("view-subscription-batch-session");
    }

}
