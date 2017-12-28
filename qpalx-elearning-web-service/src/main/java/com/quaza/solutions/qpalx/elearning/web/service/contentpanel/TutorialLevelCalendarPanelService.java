package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.google.common.collect.ImmutableMap;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalxUserTypeE;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.ContentAdminProfileRecord;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.profile.StudentEnrolmentRecord;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialGrade;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.StudentTutorialLevel;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.TutorialLevelCalendar;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.DefaultContentAdminProfileRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IContentAdminProfileRecordService;
import com.quaza.solutions.qpalx.elearning.service.qpalxuser.profile.IStudentEnrollmentRecordService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.IQPalXTutorialService;
import com.quaza.solutions.qpalx.elearning.service.tutoriallevel.ITutorialLevelCalendarService;
import com.quaza.solutions.qpalx.elearning.web.service.enums.TutorialCalendarPanelE;
import com.quaza.solutions.qpalx.elearning.web.service.user.IQPalXUserWebService;
import com.quaza.solutions.qpalx.elearning.web.service.utils.IWebAttributesUtil;
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
@Service(TutorialLevelCalendarPanelService.SPRING_BEAN)
public class TutorialLevelCalendarPanelService implements ITutorialLevelCalendarPanelService {



    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.WebAttributesUtil")
    private IWebAttributesUtil iWebAttributesUtil;

    @Autowired
    @Qualifier("com.quaza.solutions.qpalx.elearning.web.service.QPalXUserWebService")
    private IQPalXUserWebService iqPalXUserWebService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.CacheEnabledQPalXTutorialService")
    private IQPalXTutorialService iqPalXTutorialService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultTutorialLevelCalendarService")
    private ITutorialLevelCalendarService iTutorialLevelCalendarService;

    @Autowired
    @Qualifier("quaza.solutions.qpalx.elearning.service.DefaultStudentEnrollmentRecordService")
    private IStudentEnrollmentRecordService iStudentEnrollmentRecordService;

    @Autowired
    @Qualifier(DefaultContentAdminProfileRecordService.SPRING_BEAN)
    private IContentAdminProfileRecordService iContentAdminProfileRecordService;

    public static final String SPRING_BEAN = "com.quaza.solutions.qpalx.elearning.web.service.TutorialLevelCalendarPanelService";


    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TutorialLevelCalendarPanelService.class);

    @Override
    public void addCalendarPanelInfo(Model model, Long selectedTutorialLevelCalendarID) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(selectedTutorialLevelCalendarID, "selectedTutorialLevelCalendarID cannot be null");

        // Lookup the selected tutorial level calendar
        TutorialLevelCalendar selectedTutorialLevelCalendar = iTutorialLevelCalendarService.findByID(selectedTutorialLevelCalendarID);
        addCalendarPanelInfo(model, selectedTutorialLevelCalendar);
    }

    @Override
    public void addCalendarPanelInfo(Model model, TutorialLevelCalendar selectedTutorialLevelCalendar) {
        Assert.notNull(model, "model cannot be null");
        Assert.notNull(selectedTutorialLevelCalendar, "selectedTutorialLevelCalendar cannot be null");

        Optional<QPalXUser> optionalUser = iqPalXUserWebService.getLoggedInQPalXUser();
        if(optionalUser.isPresent()  && optionalUser.get().getUserType() == QPalxUserTypeE.STUDENT) {
            addCalendarPanelInfoStudent(optionalUser.get(), model, selectedTutorialLevelCalendar);
        } else if(optionalUser.isPresent()  && optionalUser.get().getUserType() == QPalxUserTypeE.CONTENT_DEVELOPER) {
            addCalendarPanelInfoContentDeveloper(optionalUser.get(), model, selectedTutorialLevelCalendar);
        }
    }

    private void addCalendarPanelInfoStudent(QPalXUser qPalXUser, Model model, TutorialLevelCalendar selectedTutorialLevelCalendar) {
        LOGGER.debug("adding calendar panel infor for Student: {}", qPalXUser.getEmail());

        // Get the student enrolment record to determine their tutorial level and grade.
        StudentEnrolmentRecord studentEnrolmentRecord = iStudentEnrollmentRecordService.findCurrentStudentEnrolmentRecord(qPalXUser);
        StudentTutorialGrade studentTutorialGrade = studentEnrolmentRecord.getStudentTutorialGrade();
        StudentTutorialLevel studentTutorialLevel = studentTutorialGrade.getStudentTutorialLevel();
        List<TutorialLevelCalendar> tutorialLevelCalendars = iTutorialLevelCalendarService.findAllByStudentTutorialLevel(studentTutorialLevel);

        // Add all attributes to mode
        ImmutableMap<String, Object> tutorialLevelPanelAttributes = TutorialCalendarPanelE.buildAttributes(tutorialLevelCalendars, selectedTutorialLevelCalendar);
        iWebAttributesUtil.addWebAttributes(model, tutorialLevelPanelAttributes);
    }

    private void addCalendarPanelInfoContentDeveloper(QPalXUser qPalXUser, Model model, TutorialLevelCalendar selectedTutorialLevelCalendar) {
        LOGGER.debug("adding calendar panel infor for Content Developer: {}", qPalXUser.getEmail());

        // Get the student enrolment record to determine their tutorial level and grade.
        ContentAdminProfileRecord contentAdminProfileRecord = iContentAdminProfileRecordService.findEnabledContentAdminProfileRecord(qPalXUser);
        StudentTutorialLevel studentTutorialLevel = contentAdminProfileRecord.getStudentTutorialLevel();
        List<TutorialLevelCalendar> tutorialLevelCalendars = iTutorialLevelCalendarService.findAllByStudentTutorialLevel(studentTutorialLevel);

        // Add all attributes to mode
        ImmutableMap<String, Object> tutorialLevelPanelAttributes = TutorialCalendarPanelE.buildAttributes(tutorialLevelCalendars, selectedTutorialLevelCalendar);
        iWebAttributesUtil.addWebAttributes(model, tutorialLevelPanelAttributes);
    }


}
