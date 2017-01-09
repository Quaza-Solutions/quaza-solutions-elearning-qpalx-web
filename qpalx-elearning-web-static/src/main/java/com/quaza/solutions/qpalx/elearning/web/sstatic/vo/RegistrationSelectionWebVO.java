package com.quaza.solutions.qpalx.elearning.web.sstatic.vo;


import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;

/**
 * @author manyce400
 */
public class RegistrationSelectionWebVO {


    @NotNull
    private String signUpType;

    public RegistrationSelectionWebVO() {

    }

    public String getSignUpType() {
        return signUpType;
    }

    public RegistrationTypeE getAsSignUpTypeE() {
        return RegistrationTypeE.getSignUpTypeE(signUpType);
    }

    public void setSignUpType(String signUpType) {
        this.signUpType = signUpType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("signUpType", signUpType)
                .toString();
    }
}
