package com.quaza.solutions.qpalx.elearning.web.zone.content.student.progress;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.FactorAffectingProficiencyRanking;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.CummulativeProficiencyRankingService;
import com.quaza.solutions.qpalx.elearning.service.lms.adaptivelearning.ICummulativeProficiencyRankingService;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IELearningCurriculumService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Controller
public class AdaptivePerformanceController {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier(CummulativeProficiencyRankingService.DEFAULT_SPRING_BEAN)
    private ICummulativeProficiencyRankingService iCummulativeProficiencyRankingService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledELearningCurriculumService")
    private IELearningCurriculumService ieLearningCurriculumService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AdaptivePerformanceController.class);


    @RequestMapping(value = "/performance/calculate-performance-ranking/curriculum/{id}", method = RequestMethod.GET)
    public void dispalyEBooksPromo(@PathVariable("id") Long id, Model model, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        LOGGER.info("Calculating new Students performance ranking in Curriculum with id: {}", id);

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();

        ELearningCurriculum eLearningCurriculum = ieLearningCurriculumService.findByELearningCurriculumID(1L);
        List<FactorAffectingProficiencyRanking> factorsAffectingProficiencyRankingList = iCummulativeProficiencyRankingService.computeAndSaveStudentAdaptiveProficiencyRankingInCurriculum(optionalUser.get(), eLearningCurriculum);
        String targetURL = "/curriculum-courses?curriculumID=" + id;
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, targetURL);
    }


}
