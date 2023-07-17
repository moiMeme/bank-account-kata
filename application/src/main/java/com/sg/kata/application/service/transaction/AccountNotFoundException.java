package com.sg.kata.application.service.transaction;

import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;

public class AccountNotFoundException extends CommonException {
    public AccountNotFoundException(String accountId) {
        super(ErrorCode.EXP010, "Account", accountId);
    }
}
