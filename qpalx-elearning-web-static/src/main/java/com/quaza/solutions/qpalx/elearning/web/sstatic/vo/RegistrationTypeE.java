package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;

import org.springframework.util.Assert;

/**
 * Defines the available types of Sign-up types available on the QPalX platform.
 *
 * @author manyce400
 */
public enum RegistrationTypeE {


    Student,
    Parent,
    Teacher
    ;

    public static RegistrationTypeE getSignUpTypeE(String signUpType) {
        Assert.notNull(signUpType, "signUpType cannot be null");

        for(RegistrationTypeE registrationTypeE : values()) {
            if(registrationTypeE.toString().equals(signUpType)) {
                return registrationTypeE;
            }
        }

        return null;
    }

}
