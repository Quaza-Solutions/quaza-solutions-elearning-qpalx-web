package com.quaza.solutions.qpalx.elearning.web.service.quiz.maker;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author manyce400
 */
public class ClassicQuizMakerError {


    private List<String> errors = new ArrayList<>();

    public static final ClassicQuizMakerError NO_ERRORS = new ClassicQuizMakerError();

    public ClassicQuizMakerError() {

    }

    public Optional<List<String>> getErrors() {
        if(errors.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(errors);
    }

    public void addQuizError(String error) {
        Assert.notNull(error);
        errors.add(error);
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }


    public static ClassicQuizMakerError of(String error) {
        ClassicQuizMakerError classicQuizMakerError = new ClassicQuizMakerError();
        classicQuizMakerError.addQuizError(error);
        return classicQuizMakerError;
    }


}
