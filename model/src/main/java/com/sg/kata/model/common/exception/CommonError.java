package com.sg.kata.model.common.exception;


import java.io.Serializable;

public interface CommonError extends Serializable {

    default String getCode() {
        return name();
    }

    String name();
    String getDescription();

}
