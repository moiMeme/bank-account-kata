package com.sg.kata.application.port.in.account;

import com.sg.kata.model.account.AccountId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface GetBalanceUserCase {
    BigDecimal getBalance(AccountId accountId, LocalDateTime beforeThisDate, boolean inclusive);

}
