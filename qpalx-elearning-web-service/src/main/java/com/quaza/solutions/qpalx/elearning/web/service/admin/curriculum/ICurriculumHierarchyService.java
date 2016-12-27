package com.quaza.solutions.qpalx.elearning.web.service.admin.curriculum;

import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXELessonWebVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QPalXEMicroLessonVO;
import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.QuestionBankVO;

/**
 * @author manyce400
 */
public interface ICurriculumHierarchyService {

    public void buildHierarchyForQPalXELessonWebVO(QPalXELessonWebVO qPalXELessonWebVO);

    public void buildHierarchyForQPalXEMicroLessonVO(QPalXEMicroLessonVO qPalXEMicroLessonVO);

    public void buildHierarchyForQuestionBankVO(QuestionBankVO questionBankVO);

}
