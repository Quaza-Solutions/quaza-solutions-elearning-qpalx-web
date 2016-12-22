package com.quaza.solutions.qpalx.elearning.web.service.enums;

import com.google.common.collect.ImmutableMap;
import com.quaza.solutions.qpalx.elearning.domain.tutoriallevel.TutorialLevelCalendar;

import java.util.List;

/**
 * @author manyce400
 */
public enum TutorialCalendarPanelE {

    TutorialCalendarList,

    SelectedTutorialCalendar;

    public static ImmutableMap<String, Object> buildAttributes(List<TutorialLevelCalendar> studentTutorialLevelCalendar , TutorialLevelCalendar selectedTutorialLevelCalendar) {
        ImmutableMap<String, Object> attributes = ImmutableMap.of(
                SelectedTutorialCalendar.toString(), selectedTutorialLevelCalendar,
                TutorialCalendarList.toString(), studentTutorialLevelCalendar
        );

        return  attributes;
    }
}
