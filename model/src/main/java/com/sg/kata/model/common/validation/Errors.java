package com.sg.kata.model.common.validation;

import com.sg.kata.model.common.exception.ErrorCode;

import java.util.ArrayList;

import static com.sg.kata.model.common.Util.formatErrorMessage;

public class Errors extends ArrayList<String> {

    public void add(ErrorCode errorCode, Object... args) {
        this.add(formatErrorMessage(errorCode, args));
    }

}
