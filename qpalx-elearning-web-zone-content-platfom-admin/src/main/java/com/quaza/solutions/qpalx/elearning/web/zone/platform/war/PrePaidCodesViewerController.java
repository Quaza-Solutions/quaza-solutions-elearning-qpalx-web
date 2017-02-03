package com.quaza.solutions.qpalx.elearning.web.zone.platform.war;

import com.quaza.solutions.qpalx.elearning.domain.subscription.PrepaidSubscriptionStatistic;
import com.quaza.solutions.qpalx.elearning.domain.subscription.SubscriptionCodeBatchSession;
import com.quaza.solutions.qpalx.elearning.domain.subscription.SubscriptionCodeBatchSessionStatistic;
import com.quaza.solutions.qpalx.elearning.service.geographical.DefaultGeographicalDateTimeFormatter;
import com.quaza.solutions.qpalx.elearning.service.prepaidsubscription.IQPalxPrepaidIDService;
import com.quaza.solutions.qpalx.elearning.service.subscription.ISubscriptionCodeBatchSessionService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.PlatformAdminManagementModeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.SubscriptionManagementAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.utils.excel.PrepaidSubscriptionStatisticExcelView;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Find all Subscription batch statistics and display.
        List<SubscriptionCodeBatchSessionStatistic> allSubscriptionCodeBatchSessionStatistics = iSubscriptionCodeBatchSessionService.findAllSubscriptionCodeBatchSessionStatistic();
        model.addAttribute(SubscriptionManagementAttributeE.AllSubscriptionCodeBatchSessionStatistics.toString(), allSubscriptionCodeBatchSessionStatistics);

        return ContentRootE.Platform_Admin_Prepaid_Codes.getContentRootPagePath("view-subscription-batch-session");
    }

    @PreAuthorize("hasAuthority('PLATFORM_ADMIN')")
    @RequestMapping(value = "/view-all-prepaid-subscription-codes-excel", method = RequestMethod.GET)
    public ModelAndView generateAllPrepaidSubscriptionCodesExcel(Model model, @RequestParam("subscriptionCodeBatchSessionID") String subscriptionCodeBatchSessionID, HttpServletResponse response) {
        LOGGER.info("Generating prepaid subscription excel codes for subscriptionCodeBatchSessionID: {}", subscriptionCodeBatchSessionID);

        Map<String, Object> excelViewDataMap = new HashMap<>();
        Long id = NumberUtils.toLong(subscriptionCodeBatchSessionID);
        SubscriptionCodeBatchSession subscriptionCodeBatchSession = iSubscriptionCodeBatchSessionService.findByID(id);

        List<PrepaidSubscriptionStatistic> prepaidSubscriptionStatisticList  = iSubscriptionCodeBatchSessionService.findAllSubscriptionBatchPrepaidSubscriptionStatistic(id);
        excelViewDataMap.put(SubscriptionManagementAttributeE.SubscriptionCodeBatchSession.toString(), subscriptionCodeBatchSession);
        excelViewDataMap.put(SubscriptionManagementAttributeE.PrepaidSubscriptionStatisticList.toString(), prepaidSubscriptionStatisticList);

        // Set header type details
        String fileName = new StringBuffer()
                .append(subscriptionCodeBatchSession.getSubscriptionCodeBatchSessionUID())
                .append(".xls")
                .toString();
        response.setContentType( "application/ms-excel" );
        response.setHeader("Content-disposition", "attachment; filename=" + fileName);

        return new ModelAndView(new PrepaidSubscriptionStatisticExcelView(DefaultGeographicalDateTimeFormatter.DATE_TIME_DISPLAY_FORMATTER), excelViewDataMap);
    }

}
