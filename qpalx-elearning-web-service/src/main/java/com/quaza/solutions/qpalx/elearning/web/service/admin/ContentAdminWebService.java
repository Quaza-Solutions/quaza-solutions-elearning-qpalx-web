package com.quaza.solutions.qpalx.elearning.web.service.admin;

import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.ELearningCurriculum;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.service.lms.curriculum.IStudentCurriculumService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IContentAdminProfileRecordService;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
@Service("com.quaza.solutions.qpalx.elearning.web.service.ContentAdminWebService")
public class ContentAdminWebService implements IContentAdminWebService {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.StudentCurriculumService")
    private IStudentCurriculumService iStudentCurriculumService;


    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultContentAdminProfileRecordService")
    private IContentAdminProfileRecordService iContentAdminProfileRecordService;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ContentAdminWebService.class);


    @Override
    public void addContentAdminCurriculaOptions(Model model) {
        Assert.notNull(model, "model cannot be null");
        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        LOGGER.info("Adding content admin curricula options data for user:> {}", optionalUser.get().getEmail());

        // Get and set all the Student Tutorial Grades assiged to content admin user
        List<StudentTutorialGrade> studentTutorialGrades = iContentAdminProfileRecordService.findContentAdminStudentTutorialGrades(optionalUser.get());
        model.addAttribute("AssignedStudentTutorialGrades", studentTutorialGrades);

        // Load up all curricula for first studentTutorialGrades
        StudentTutorialGrade studentTutorialGrade = studentTutorialGrades.get(0);
        List<ELearningCurriculum> eLearningCurricula = iStudentCurriculumService.findAllCoreELearningCurriculum(studentTutorialGrade);
        model.addAttribute("SelectedStudentTutorialGrade", studentTutorialGrade.getTutorialGrade());
        model.addAttribute("AssignedELearningCurricula", eLearningCurricula);
    }

}
