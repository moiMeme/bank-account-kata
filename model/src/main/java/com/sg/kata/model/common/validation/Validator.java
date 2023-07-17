package com.sg.kata.model.common.validation;

public abstract class Validator {

    public Errors validate() {
        return ValidatorUtils.validate(this);
    }

}
