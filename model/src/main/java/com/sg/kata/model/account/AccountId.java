package com.sg.kata.model.account;

import java.util.Objects;

public record AccountId(String value) {

    public static final int ACCOUNT_NUMBER_LENGTH = 20;

    public AccountId {
        Objects.requireNonNull(value, "'value' must not be null");
        if (value.length() != ACCOUNT_NUMBER_LENGTH) {
            throw new IllegalArgumentException("'value' length must be " + ACCOUNT_NUMBER_LENGTH);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId = (AccountId) o;
        return Objects.equals(value, accountId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
