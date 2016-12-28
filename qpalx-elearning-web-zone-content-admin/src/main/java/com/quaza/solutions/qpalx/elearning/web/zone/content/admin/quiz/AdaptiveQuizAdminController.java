package com.quaza.solutions.qpalx.elearning.web.zone.content.admin.quiz;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXEMicroLesson;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IQPalXEMicroLessonService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.AdaptiveLearningQuizAttributeE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.ContentRootE;
import com.quaza.solutions.qpalx.elearning.web.service.enums.CurriculumDisplayAttributeE;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.AdaptiveLearningQuizWebVO;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @author manyce400
 */
@Controller
@SessionAttributes(value = {"SelectedQPalXMicroLesson", "AdminAdaptiveLearningQuizWebVO"})
public class AdaptiveQuizAdminController {




    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.QPalXEMicroLessonService")
    private IQPalXEMicroLessonService iqPalXEMicroLessonService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdaptiveQuizAdminController.class);


    @RequestMapping(value = "/create-qpalx-quiz", method = RequestMethod.GET)
    public String addAdaptiveQuiz(Model model, ModelMap modelMap, @RequestParam("microLessonID") String microLessonID) {
        LOGGER.info("Processing request to add Adaptive Quiz for microLessonID: {}", microLessonID);

        // Register AdaptiveLearningQuizWebVO on modelMap to enable session tracking for all quiz pages
        AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO = new AdaptiveLearningQuizWebVO();
        modelMap.addAttribute(AdaptiveLearningQuizAttributeE.AdminAdaptiveLearningQuizWebVO.toString(), adaptiveLearningQuizWebVO);

        Long id = NumberUtils.toLong(microLessonID);
        QPalXEMicroLesson qPalXEMicroLesson = iqPalXEMicroLessonService.findByID(id);
        modelMap.addAttribute(CurriculumDisplayAttributeE.SelectedQPalXMicroLesson.toString(), qPalXEMicroLesson);
        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("add-adaptive-quiz-details");
    }

    @RequestMapping(value = "/process-quiz-details", method = RequestMethod.POST)
    public String saveAdaptiveQuizDetails(Model model, ModelMap modelMap, @ModelAttribute("SelectedQPalXMicroLesson") QPalXEMicroLesson qPalXEMicroLesson,
                                          @ModelAttribute("AdminAdaptiveLearningQuizWebVO") AdaptiveLearningQuizWebVO adaptiveLearningQuizWebVO, HttpServletRequest request) {
        LOGGER.info("Attempting to save Adaptive Quiz details for adaptiveLearningQuizWebVO: {}", adaptiveLearningQuizWebVO);

        Map params = request.getParameterMap();
        Iterator i = params.keySet().iterator();
        while ( i.hasNext() )
        {
            String key = (String) i.next();
            String value = ((String[]) params.get( key ))[ 0 ];
            System.out.println("key = " + key + " value: " + value);
        }

        return ContentRootE.Content_Admin_Quiz.getContentRootPagePath("customize-adaptive-quiz");
    }
}