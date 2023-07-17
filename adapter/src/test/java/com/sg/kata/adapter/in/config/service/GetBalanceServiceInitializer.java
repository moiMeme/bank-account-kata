package com.sg.kata.adapter.in.config.service;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.adapter.in.config.ds.TransactionDSGatewayInitializer;
import com.sg.kata.application.port.in.account.GetBalanceUserCase;
import com.sg.kata.application.service.account.GetBalanceService;

public class GetBalanceServiceInitializer extends LazyInitializer<GetBalanceUserCase> {

    private final TransactionDSGatewayInitializer transactionDSGateway;

    public GetBalanceServiceInitializer(TransactionDSGatewayInitializer transactionDSGateway) {
        this.transactionDSGateway = transactionDSGateway;
    }

    @Override
    protected GetBalanceUserCase initialize() {
        return new GetBalanceService(transactionDSGateway.get());
    }
}
