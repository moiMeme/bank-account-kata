package com.sg.kata.application.service.account;

import com.sg.kata.application.port.in.account.GetBalanceUserCase;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.Util;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GetBalanceService implements GetBalanceUserCase {
    private final TransactionDSGateway transactionDSGateway;

    public GetBalanceService(TransactionDSGateway transactionDSGateway) {
        this.transactionDSGateway = transactionDSGateway;
    }

    @Override
    public BigDecimal getBalance(AccountId accountId, LocalDateTime beforeThisDate, boolean inclusive) {
        Util.requireNonNull(accountId, "accountId");
        Util.requireNonNull(beforeThisDate, "beforeThisDate");
        return transactionDSGateway.findBalanceByAccountAndDate(accountId, beforeThisDate, inclusive);
    }

}
