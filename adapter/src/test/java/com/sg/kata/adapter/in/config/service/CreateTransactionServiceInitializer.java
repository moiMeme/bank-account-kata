package com.sg.kata.adapter.in.config.service;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.adapter.in.config.ds.AccountDSGatewayInitializer;
import com.sg.kata.adapter.in.config.ds.TransactionDSGatewayInitializer;
import com.sg.kata.adapter.in.config.factory.TransactionFactoryInitializer;
import com.sg.kata.application.port.in.transaction.CreateTransactionUseCase;
import com.sg.kata.application.service.transaction.CreateTransactionService;

public class CreateTransactionServiceInitializer extends LazyInitializer<CreateTransactionUseCase> {

    private final AccountDSGatewayInitializer accountDSGateway;
    private final TransactionDSGatewayInitializer transactionDSGateway;
    private final TransactionFactoryInitializer transactionFactory;

    public CreateTransactionServiceInitializer(AccountDSGatewayInitializer accountDSGateway, TransactionDSGatewayInitializer transactionDSGateway, TransactionFactoryInitializer transactionFactory) {
        this.accountDSGateway = accountDSGateway;
        this.transactionDSGateway = transactionDSGateway;
        this.transactionFactory = transactionFactory;
    }

    @Override
    protected CreateTransactionUseCase initialize() {
        return new CreateTransactionService(accountDSGateway.get(), transactionDSGateway.get(), transactionFactory.get());
    }
}
