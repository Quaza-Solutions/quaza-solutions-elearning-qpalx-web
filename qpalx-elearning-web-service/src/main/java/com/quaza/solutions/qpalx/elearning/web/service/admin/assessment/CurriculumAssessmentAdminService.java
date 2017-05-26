package com.quaza.solutions.qpalx.elearning.web.service.admin.assessment;

import com.quaza.solutions.qpalx.elearning.web.sstatic.vo.CourseAssessmentFocusAreaVO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * @author manyce400
 */
@Service(CurriculumAssessmentAdminService.BEAN_NAME)
public class CurriculumAssessmentAdminService implements ICurriculumAssessmentAdminService {




    private List<CourseAssessmentFocusAreaVO> courseAssessmentFocusAreaVOList = new LinkedList<CourseAssessmentFocusAreaVO>();

    public static final String BEAN_NAME = "com.quaza.solutions.qpalx.elearning.web.service.admin.assessment.CurriculumAssessmentAdminService";

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CurriculumAssessmentAdminService.class);


    @Override
    public void addCourseAssessmentFocusAreaVO(CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO) {
        Assert.notNull(courseAssessmentFocusAreaVO, "courseAssessmentFocusAreaVO cannot be null");
        Assert.isNull(courseAssessmentFocusAreaVO.getFocusAreaUniqueID(), "courseAssessmentFocusAreaVO has already been added to this model");
        LOGGER.debug("Recording course assessment foucus area: {}", courseAssessmentFocusAreaVO);
        Long currentListSize = new Long(courseAssessmentFocusAreaVOList.size() + 1);
        courseAssessmentFocusAreaVO.setFocusAreaUniqueID(currentListSize);
        courseAssessmentFocusAreaVOList.add(courseAssessmentFocusAreaVO);
    }

    @Override
    public CourseAssessmentFocusAreaVO findCourseAssessmentFocusAreaVOByUniqueID(Long focusAreaUniqueID) {
        Assert.notNull(focusAreaUniqueID, "focusAreaUniqueID cannot be null");
        LOGGER.debug("Finding CourseAssessmentFocusAreaVO with focusAreaUniqueID: {}", focusAreaUniqueID);

        for(CourseAssessmentFocusAreaVO courseAssessmentFocusAreaVO : courseAssessmentFocusAreaVOList) {
            if(courseAssessmentFocusAreaVO.getFocusAreaUniqueID() == focusAreaUniqueID) {
                return courseAssessmentFocusAreaVO;
            }
        }

        return null;
    }

}
