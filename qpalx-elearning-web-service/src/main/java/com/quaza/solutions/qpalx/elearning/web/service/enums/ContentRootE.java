package com.quaza.solutions.qpalx.elearning.web.service.enums;

/**
 * Defines all the root content directories for all QPalX web content pages.
 *
 * @author manyce400
 */
public enum ContentRootE {


    Access_Denied_403("404"),

    Home(""),

    Student_Home("student-user/home/"),

    Student_Signup("student-user/signup/"),

    Student_Adaptive_Learning("student-user/adaptive-learning/"),

    Student_Adaptive_Learning_Quiz("student-user/adaptive-learning-quiz/"),

    Guardian_Home("guardian-user/home/"),

    Guardian_Signup("guardian-user/signup/"),

    Content_Admin_Home("content-admin/home/"),

    Content_Admin_Lessons("content-admin/lessons/"),

    Content_Admin_Quiz("content-admin/adaptive-quiz/"),

    Platform_Admin_Home("platform-admin/home/"),

    Platform_Admin_Prepaid_Codes("platform-admin/prepaid-codes/")
    ;

    private final String rootDirectory;

    ContentRootE(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }

    public String getContentRootPagePath(String page) {
        return  new StringBuffer(rootDirectory)
                .append(page)
                .toString();
    }

}
