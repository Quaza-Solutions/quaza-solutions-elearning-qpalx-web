package com.quaza.solutions.qpalx.elearning.web.service.proficiency;

import com.quaza.solutions.qpalx.elearning.domain.lms.adaptivelearning.ProficiencyRankingCompuationResult;
import com.quaza.solutions.qpalx.elearning.domain.lms.curriculum.CurriculumType;
import com.quaza.solutions.qpalx.elearning.domain.qpalxuser.QPalXUser;

import java.util.List;

/**
 * @author manyce400
 */
public interface ICummulativeProficiencyRankingService {


    public List<ProficiencyRankingCompuationResult> computeAndRecordStudentProficienciesByCurriculumType(QPalXUser qPalXUser, CurriculumType curriculumType);

}
