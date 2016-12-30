package com.quaza.solutions.qpalx.elearning.web.service.contentpanel;

import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.TutorialLevelCalendar;
import org.springframework.ui.Model;

/**
 * @author manyce400
 */
public interface ITutorialLevelCalendarPanelService {


    public void addCalendarPanelInfo(Model model, Long selectedTutorialLevelCalendarID);

    public void addCalendarPanelInfo(Model model, TutorialLevelCalendar selectedTutorialLevelCalendar);

}
