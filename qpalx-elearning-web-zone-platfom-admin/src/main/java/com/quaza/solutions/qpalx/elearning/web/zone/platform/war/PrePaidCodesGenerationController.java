package com.quaza.solutions.qpalx.elearning.web.zone.platform.war;

import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXCountry;
import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXMunicipality;
import com.quaza.solutions.qpalx.elearning.domain.subscription.QPalXSubscription;
import com.quaza.solutions.qpalx.elearning.service.geographical.IQPalXMunicipalityService;
import com.quaza.solutions.qpalx.elearning.service.prepaidsubscription.IQPalxPrepaidIDService;
import com.quaza.solutions.qpalx.elearning.service.subscription.IQPalxSubscriptionService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserInfoPanelService;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXWebUserVO;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trading_1 on 5/24/2016.
 */
@Controller
public class PrePaidCodesGenerationController {
    private static final String FILE_PATH = "/temp-uploads/RequestedPrepaidCodes.xls";//gets file from browser generation
    private static final String APPLICATION_XLS = "application/xls";

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxPrepaidIDService")
    private IQPalxPrepaidIDService iQpalxPrepaidIDService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXMunicipalityService")
    private IQPalXMunicipalityService iQPalXMunicipalityService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultQPalxSubscriptionService")
    private IQPalxSubscriptionService iQPalxSubscriptionService;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserInfoPanelService")
    private IQPalXUserInfoPanelService qPalXUserInfoPanelService;


    @PreAuthorize("hasAuthority('PLATFORM_ADMIN')")
    @RequestMapping(value = "/generate-prepaid-codes", method = RequestMethod.GET)
    public String startGenrateIds(Model model) {
        List<QPalXMunicipality> municipalities = iQPalXMunicipalityService.findAllQPalXMunicipalities();
        List<QPalXSubscription> subscriptions = iQPalxSubscriptionService.findAllSubscriptions();
        List<QPalXCountry> countries = new ArrayList<QPalXCountry>();

        for(int i =0; i<municipalities.size(); i++){
            if(!countries.contains(municipalities.get(i).getQPalXCountry())){
                countries.add(municipalities.get(i).getQPalXCountry());
            }
        }


        // Add all attributes required for User information panel
        qPalXUserInfoPanelService.addUserInfoAttributes(model);
        model.addAttribute(CurriculumDisplayAttributeE.DisplayUserInfo.toString(), Boolean.TRUE.toString());

        QPalXWebUserVO qPalXWebUserVO = new QPalXWebUserVO();
        qPalXWebUserVO.setNumToGenerate(0);
        model.addAttribute("QPalXCountries", countries);
        model.addAttribute("QPalXWebUserVO", qPalXWebUserVO);
        model.addAttribute("QPalXMunicipalities", municipalities);
        model.addAttribute("QPalXSubscriptions", subscriptions);
        return ContentRootE.Platform_Admin_Prepaid_Codes.getContentRootPagePath("prepaid-codes-gen");
    }

    @RequestMapping(value = "/generateIds", method=RequestMethod.POST)//value/generate should be something else and the form should be generateIds
    public void generateExcel(@ModelAttribute(value="QPalXWebUserVO") QPalXWebUserVO qPalXWebUserVO, HttpServletResponse response) throws Exception{
        QPalXSubscription qPalXSubscription = iQPalxSubscriptionService.findQPalXSubscriptionByID(qPalXWebUserVO.getSubscriptionID());
        QPalXMunicipality qPalXMunicipality = iQPalXMunicipalityService.findQPalXMunicipalityByID(qPalXWebUserVO.getMunicipalityID());

        int numOfCodes = qPalXWebUserVO.getNumToGenerate();
        iQpalxPrepaidIDService.generateAndWritePrepaidIdsToExcel(numOfCodes, qPalXMunicipality, qPalXSubscription, "RequestedPrepaidCodes");
        File file = new File(FILE_PATH);

        InputStream in = new FileInputStream(file);
        response.setContentType(APPLICATION_XLS);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        ServletOutputStream sout = response.getOutputStream();
        FileCopyUtils.copy(in, sout);
        IOUtil.closeQuietly(in);
        response.flushBuffer();
        file.delete();
    }



}
