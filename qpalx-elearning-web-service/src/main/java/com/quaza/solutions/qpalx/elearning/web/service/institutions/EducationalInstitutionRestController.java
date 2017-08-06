package com.quaza.solutions.qpalx.elearning.web.service.institutions;

import com.google.common.collect.ImmutableList;
import com.quaza.solutions.qpalx.elearning.domain.geographical.QPalXMunicipality;
import com.quaza.solutions.qpalx.elearning.domain.institutions.QPalXEducationalInstitution;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.AcademicLevelE;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialLevel;
import com.quaza.solutions.qpalx.elearning.service.geographical.CacheEnabledQPalXMunicipalityService;
import com.quaza.solutions.qpalx.elearning.service.geographical.IQPalXMunicipalityService;
import com.quaza.solutions.qpalx.elearning.service.institutions.DefaultQPalXEducationalInstitutionService;
import com.quaza.solutions.qpalx.elearning.service.institutions.IQPalXEducationalInstitutionService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.CacheEnabledQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author manyce400
 */
@RestController
public class EducationalInstitutionRestController implements IEducationalInstitutionRestController {



    @Autowired
    @Qualifier(CacheEnabledQPalXTutorialService.SPRING_BEAN)
    private IQPalXTutorialService iqPalXTutorialService;

    @Autowired
    @Qualifier(CacheEnabledQPalXMunicipalityService.SPRING_BEAN)
    private IQPalXMunicipalityService iqPalXMunicipalityService;

    @Autowired
    @Qualifier(DefaultQPalXEducationalInstitutionService.SPRING_BEAN)
    private IQPalXEducationalInstitutionService iqPalXEducationalInstitutionService;


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(EducationalInstitutionRestController.class);


    @CrossOrigin
    @RequestMapping(value = "/find-academic-levels-by-municipality", method = RequestMethod.GET)
    @Override
    public List<StudentTutorialLevel> findAllAcademicLevelsForMunicipality(@RequestParam(value = "municipalityID") Long municipalityID) {
        Assert.notNull(municipalityID, "municipalityID cannot be null");

        LOGGER.info("Finding all StudentTutorialLevel in municipalityID:> {}", municipalityID);
        QPalXMunicipality qPalXMunicipality = iqPalXMunicipalityService.findQPalXMunicipalityByID(municipalityID);

        return iqPalXTutorialService.findStudentTutorialLevelsByMunicipality(qPalXMunicipality);
    }

    @CrossOrigin
    @RequestMapping(value = "/find-student-tutorial-grades-by-academic-level", method = RequestMethod.GET)
    @Override
    public List<StudentTutorialGrade> findAllStudentTutoralGradesForAcademicLevel(@RequestParam(value = "studentTutorialLevelID") Long studentTutorialLevelID) {
        Assert.notNull(studentTutorialLevelID, "studentTutorialLevelID cannot be null");
        LOGGER.info("Finding all StudentTutorialGrades for tutorialLevelID: {}", studentTutorialLevelID);
        StudentTutorialLevel studentTutorialLevel = iqPalXTutorialService.findQPalXTutorialLevelByID(studentTutorialLevelID);
        return iqPalXTutorialService.findAllStudentTutorialGradeByTutorialLevel(studentTutorialLevel);
    }

    @CrossOrigin
    @RequestMapping(value = "/FindEducationalInstitutionsMatching", method = RequestMethod.GET)
    @Override
    public List<QPalXEducationalInstitution> findAllEducationalInstitutions(@RequestParam(value = "municipalityID") Long municipalityID, @RequestParam(value = "studentTutorialLevelID") Long studentTutorialLevelID) {
        Assert.notNull(municipalityID, "municipalityID cannot be null");
        Assert.notNull(studentTutorialLevelID, "");

        LOGGER.info("Finding all EductationalInstitutions matching municipalityID:> {} and studentTutorialLevelID:> {}", municipalityID, studentTutorialLevelID);

        QPalXMunicipality qPalXMunicipality = iqPalXMunicipalityService.findQPalXMunicipalityByID(municipalityID);
        StudentTutorialLevel studentTutorialLevel = iqPalXTutorialService.findQPalXTutorialLevelByID(studentTutorialLevelID);

        AcademicLevelE academicLevelE = studentTutorialLevel.getAcademicLevel();

        List<QPalXEducationalInstitution> results = ImmutableList.of();
        switch (academicLevelE) {
            case Primary:
                results = iqPalXEducationalInstitutionService.findAlEducationalInstitutionsInMunicipalityWithPrimaryEducation(qPalXMunicipality);
                break;
            case JuniorHigh:
                results = iqPalXEducationalInstitutionService.findAlEducationalInstitutionsInMunicipalityWithJHSEducation(qPalXMunicipality);
                break;
            case SeniorHigh:
                results = iqPalXEducationalInstitutionService.findAlEducationalInstitutionsInMunicipalityWithSHSEducation(qPalXMunicipality);
                break;
            case College:
                results = iqPalXEducationalInstitutionService.findAlEducationalInstitutionsInMunicipalityWithCollegeEducation(qPalXMunicipality);
                break;
            default:
                break;
        }


        return results;
    }
}
