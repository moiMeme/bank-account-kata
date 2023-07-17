package com.sg.kata.application.service.soa;

import com.sg.kata.application.port.in.soa.GetSOAUseCase;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.soa.Soa;
import com.sg.kata.model.soa.factory.SoaFactory;
import com.sg.kata.model.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class GetSOAService implements GetSOAUseCase {

    private final TransactionDSGateway transactionDSGateway;
    private final SoaFactory soaFactory;

    public GetSOAService(TransactionDSGateway transactionDSGateway, SoaFactory soaFactory) {
        this.transactionDSGateway = transactionDSGateway;
        this.soaFactory = soaFactory;
    }

    @Override
    public Set<Soa> getByAccountAndDate(AccountId accountNumber, LocalDateTime fromDate, LocalDateTime toDate) {
        Util.requireNonNull(accountNumber, "accountNumber");
        Util.requireNonNull(fromDate, "fromDate");
        Util.requireNonNull(toDate, "toDate");
        if (fromDate.isAfter(toDate)) {
            throw new CommonException(ErrorCode.EXP006, fromDate.format(DateTimeFormatter.ISO_DATE_TIME), toDate.format(DateTimeFormatter.ISO_DATE_TIME));
        }
        Set<Transaction> transactions = transactionDSGateway.findByAccountAndDate(accountNumber.value(), fromDate, toDate);
        return soaFactory.create(accountNumber, transactions);
    }
}
