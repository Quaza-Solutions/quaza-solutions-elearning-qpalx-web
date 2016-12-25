package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import com.google.common.collect.ImmutableSet;
import com.quaza.solutions.qpalx.elearning.domain.lms.content.hierarchy.IHierarchicalLMSContent;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.IQPalXELessonVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.QPalXELesson;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.AbstractILMSMediaContentVO;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.MediaContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.lms.media.QPalXTutorialContentTypeE;
import com.quaza.solutions.qpalx.elearning.domain.subjectmatter.proficiency.ProficiencyRankingScaleE;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Set;

/**
 * @author manyce400
 */
public class QPalXELessonWebVO extends AbstractILMSMediaContentVO implements IQPalXELessonVO {


    public String lessonName;

    public String lessonDescription;

    private String proficiencyRankingScaleFloor;

    private String proficiencyRankingScaleCeiling;

    private Long eLearningCourseID;

    private Long tutorialLevelCalendarID;

    private Long educationalInstitutionID;

    private Long qPalxELessonID;

    private IHierarchicalLMSContent iHierarchicalLMSContent;


    public QPalXELessonWebVO() {

    }

    public QPalXELessonWebVO(final QPalXELesson qPalXELesson) {
        this.qPalxELessonID = qPalXELesson.getId();
        this.lessonName = qPalXELesson.getLessonName();
        this.lessonDescription = qPalXELesson.getLessonDescription();
        this.eLearningCourseID = qPalXELesson.geteLearningCourse().getId();
        this.tutorialLevelCalendarID = qPalXELesson.getTutorialLevelCalendar().getId();
        this.proficiencyRankingScaleFloor = qPalXELesson.getProficiencyRankingScaleFloor().toString();
        this.proficiencyRankingScaleCeiling = qPalXELesson.getProficiencyRankingScaleCeiling().toString();
        this.educationalInstitutionID = qPalXELesson.getQPalXEducationalInstitution() != null ? qPalXELesson.getQPalXEducationalInstitution().getId() : null;
    }

    @Override
    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    @Override
    public String getLessonDescription() {
        return lessonDescription;
    }

    public void setLessonDescription(String lessonDescription) {
        this.lessonDescription = lessonDescription;
    }

    @Override
    public String getProficiencyRankingScaleFloor() {
        return proficiencyRankingScaleFloor;
    }

    public void setProficiencyRankingScaleFloor(String proficiencyRankingScaleFloor) {
        this.proficiencyRankingScaleFloor = proficiencyRankingScaleFloor;
    }

    @Override
    public String getProficiencyRankingScaleCeiling() {
        return proficiencyRankingScaleCeiling;
    }

    public void setProficiencyRankingScaleCeiling(String proficiencyRankingScaleCeiling) {
        this.proficiencyRankingScaleCeiling = proficiencyRankingScaleCeiling;
    }

    public Long getELearningCourseID() {
        return eLearningCourseID;
    }

    public void setELearningCourseID(Long eLearningCourseID) {
        this.eLearningCourseID = eLearningCourseID;
    }

    @Override
    public Long getTutorialLevelCalendarID() {
        return tutorialLevelCalendarID;
    }

    public void setTutorialLevelCalendarID(Long tutorialLevelCalendarID) {
        this.tutorialLevelCalendarID = tutorialLevelCalendarID;
    }

    @Override
    public Long getEducationalInstitutionID() {
        return educationalInstitutionID;
    }

    public void setEducationalInstitutionID(Long educationalInstitutionID) {
        this.educationalInstitutionID = educationalInstitutionID;
    }

    public Long getQPalxELessonID() {
        return qPalxELessonID;
    }

    public void setQPalxELessonID(Long qpalxELessonID) {
        this.qPalxELessonID = qpalxELessonID;
    }

    @Override
    public ProficiencyRankingScaleE getProficiencyRankingScaleFloorE() {
        return ProficiencyRankingScaleE.valueOf(proficiencyRankingScaleFloor);
    }

    @Override
    public ProficiencyRankingScaleE getProficiencyRankingScaleCeilingE() {
        return ProficiencyRankingScaleE.valueOf(proficiencyRankingScaleCeiling);
    }

    @Override
    public Set<MediaContentTypeE> getMediaContentTypes() {
        return ImmutableSet.of(MediaContentTypeE.mp4);
    }

    @Override
    public Set<QPalXTutorialContentTypeE> getQPalXTutorialContentTypes() {
        return ImmutableSet.of(QPalXTutorialContentTypeE.Video);
    }

    @Override
    public IHierarchicalLMSContent getIHierarchicalLMSContent() {
        return iHierarchicalLMSContent;
    }

    public void setIHierarchicalLMSContent(IHierarchicalLMSContent iHierarchicalLMSContent) {
        this.iHierarchicalLMSContent = iHierarchicalLMSContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lessonName", lessonName)
                .append("lessonDescription", lessonDescription)
                .append("proficiencyRankingScaleFloor", proficiencyRankingScaleFloor)
                .append("proficiencyRankingScaleCeiling", proficiencyRankingScaleCeiling)
                .append("eLearningCourseID", eLearningCourseID)
                .append("tutorialLevelCalendarID", tutorialLevelCalendarID)
                .append("educationalInstitutionID", educationalInstitutionID)
                .append("activeFlag", activeFlag)
                .append("qPalXTutorialContentType", qPalXTutorialContentType)
                .append("eLearningMediaContent", eLearningMediaContent)
                .toString();
    }
}
