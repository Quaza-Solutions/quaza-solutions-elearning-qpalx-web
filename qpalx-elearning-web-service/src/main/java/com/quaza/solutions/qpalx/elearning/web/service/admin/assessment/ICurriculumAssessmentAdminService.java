package com.quaza.solutions.qpalx.elearning.web.service.admin.assessment;

import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.CourseAssessmentFocusAreaVO;

/**
 * Created by manyce400 on 5/25/17.
 */
public interface ICurriculumAssessmentAdminService {


    public void addCourseAssessmentFocusAreaVO(CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO);

    public CourseAssessmentFocusAreaVO findCourseAssessmentFocusAreaVOByUniqueID(Long focusAreaUniqueID);

}
