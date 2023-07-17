package com.sg.kata.model.common.exception;

import java.io.Serial;

import static com.sg.kata.model.common.Util.formatErrorMessage;

public class CommonException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5091192495339288952L;
    private final CommonError errorCode;

    public CommonException(CommonError error, Object... args) {
        super(formatErrorMessage(error, args));
        this.errorCode = error;
    }

    public CommonException(Throwable cause, CommonError error, Object... args) {
        super(formatErrorMessage(error, args), cause);
        this.errorCode = error;
    }

}
