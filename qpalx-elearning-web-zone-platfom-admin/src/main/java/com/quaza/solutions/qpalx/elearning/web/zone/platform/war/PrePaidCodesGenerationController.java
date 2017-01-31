package com.quaza.solutions.qpalx.elearning.web.zone.platform.war;

import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXCountry;
import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXMunicipality;
import com.quaza.solutions.qpalx.elearning.domain.partner.StrategicPlatformPartner;
import com.quaza.solutions.qpalx.elearning.domain.subscription.QPalXSubscription;
import com.quaza.solutions.qpalx.elearning.service.geographical.IQPalXMunicipalityService;
import com.quaza.solutions.qpalx.elearning.service.partner.IStrategicPlatformPartnerService;
import com.quaza.solutions.qpalx.elearning.service.subscription.IQPalxSubscriptionService;
import com.quaza.solutions.qpalx.elearning.service.subscription.ISubscriptionCodeBatchSessionService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.PlatformAdminManagementModeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.platformadmin.SubscriptionManagementAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.PrepaidSubscriptionGenVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trading_1 on 5/24/2016.
 */
@Controller
public class PrePaidCodesGenerationController {


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXMunicipalityService")
    private IQPalXMunicipalityService iQPalXMunicipalityService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxSubscriptionService")
    private IQPalxSubscriptionService iQPalxSubscriptionService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StrategicPlatformPartnerService")
    private IStrategicPlatformPartnerService iStrategicPlatformPartnerService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.SubscriptionCodeBatchSessionService")
    private ISubscriptionCodeBatchSessionService iSubscriptionCodeBatchSessionService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PrePaidCodesGenerationController.class);


    @PreAuthorize("hasAuthority('PLATFORM_ADMIN')")
    @RequestMapping(value = "/generate-prepaid-codes", method = RequestMethod.GET)
    public String accessGeneratePrepaidCodesPage(Model model) {
        List<QPalXMunicipality> municipalities = iQPalXMunicipalityService.findAllQPalXMunicipalities();
        List<QPalXSubscription> subscriptions = iQPalxSubscriptionService.findAllSubscriptions();
        List<QPalXCountry> countries = new ArrayList<QPalXCountry>();

        for(int i =0; i<municipalities.size(); i++){
            if(!countries.contains(municipalities.get(i).getQPalXCountry())){
                countries.add(municipalities.get(i).getQPalXCountry());
            }
        }

        // find all strategic partners....
        List<StrategicPlatformPartner> strategicPlatformPartners = iStrategicPlatformPartnerService.findAll();


        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        QPalXWebUserVO qPalXWebUserVO = new QPalXWebUserVO();
        qPalXWebUserVO.setNumToGenerate(0);
        model.addAttribute("QPalXCountries", countries);
        model.addAttribute("QPalXWebUserVO", qPalXWebUserVO);
        model.addAttribute(SubscriptionManagementAttributeE.PrepaidSubscriptionGenVO.toString(), new PrepaidSubscriptionGenVO());
        model.addAttribute("QPalXMunicipalities", municipalities);
        model.addAttribute("QPalXSubscriptions", subscriptions);
        model.addAttribute(SubscriptionManagementAttributeE.StrategicPlatformPartnerList.toString(), strategicPlatformPartners);


        // Enable the Platform administration mode
        model.addAttribute("PlatformAdminManagementModeE", PlatformAdminManagementModeE.SubscriptionManagementMode.toString());


        return ContentRootE.Platform_Admin_Prepaid_Codes.getContentRootPagePath("prepaid-codes-gen");
    }

    @PreAuthorize("hasAuthority('PLATFORM_ADMIN')")
    @RequestMapping(value = "/generate-save-prepaid-ids", method=RequestMethod.POST)
    public void generateAndSavePrepaidIDs(@ModelAttribute(value="PrepaidSubscriptionGenVO") PrepaidSubscriptionGenVO prepaidSubscriptionGenVO, HttpServletResponse response) throws Exception{
        LOGGER.info("Generating prepaid subscription ID for prepaidSubscriptionGenVO: {}", prepaidSubscriptionGenVO);
        iSubscriptionCodeBatchSessionService.buildNewSubscriptionBatch(prepaidSubscriptionGenVO);
    }



}
