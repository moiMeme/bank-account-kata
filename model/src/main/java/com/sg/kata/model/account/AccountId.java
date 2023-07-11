package com.sg.kata.model.account;

import java.util.Objects;

public record AccountId(String value) {

    private static final int ACCOUNT_NUMBER_LENGTH = 10;

    public AccountId {
        Objects.requireNonNull(value, "'value' must not be null");
        if (value.length() != ACCOUNT_NUMBER_LENGTH) {
            throw new IllegalArgumentException("'value' length must be " + ACCOUNT_NUMBER_LENGTH);
        }
    }

}
