package com.sg.kata.model.common;

import com.sg.kata.model.account.AccountType;
import com.sg.kata.model.common.exception.CommonError;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;

import java.text.MessageFormat;
import java.util.Currency;
import java.util.Objects;
import java.util.regex.Pattern;

public class Util {

    private Util(){
    }

    public static String accountNumber(int customerId, Currency currency, AccountType accountType) {
        return String.format("%" + 11 + "s", customerId).replace(" ", "0") +
                currency.getCurrencyCode() +
                accountType.getType();
    }

    public static <T> T requireNonNull(T obj, String objectName) {
        return requireNonNull(obj, ErrorCode.EXP005, objectName);
    }
    public static <T> T requireNonNull(T obj, ErrorCode errorCode, Object... args) {
        if (obj == null) {
            throw new CommonException(errorCode, args);
        }
        return obj;
    }

    public static String formatErrorMessage(CommonError error, Object... args) {
        return error.getCode() + " " + MessageFormat.format(error.getDescription(), args);
    }

}
