package com.quaza.solutions.qpalx.elearning.web.service.enums;

import com.google.common.collect.ImmutableMap;
import org.springframework.ui.Model;

/**
 * Enumeration that defines all the possible Error attributes that could have been generated as part of a QPalX web operation.
 *
 * @author manyce400
 */
public enum WebOperationErrorAttributesE {


    Invalid_FORM_Submission,

    Invalid_Upgrade_Operation,

    Invalid_Delete_Operation

    ;

    public static ImmutableMap<String, String> buildAttributes(String formSubmissionErrorMessage) {
        return ImmutableMap.of(
               Invalid_FORM_Submission.toString(), formSubmissionErrorMessage
        );
    }

    public static void addInvalidFormSubmissionMessage(String formSubmissionErrorMessage, Model model) {
        model.addAttribute(Invalid_FORM_Submission.toString(), formSubmissionErrorMessage);
    }

}
