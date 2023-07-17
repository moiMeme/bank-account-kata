package com.sg.kata.adapter.in.config.service;

import com.sg.kata.adapter.in.config.ds.AccountDSGatewayInitializer;
import com.sg.kata.adapter.in.config.factory.AccountFactoryInitializer;
import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.application.service.account.AddAccountService;

public class AddAccountServiceInitializer extends LazyInitializer<AddAccountUseCase> {

    private final AccountDSGatewayInitializer accountDSGateway;
    private final AccountFactoryInitializer accountFactory;

    public AddAccountServiceInitializer(AccountDSGatewayInitializer accountDSGateway, AccountFactoryInitializer accountFactory) {
        this.accountDSGateway = accountDSGateway;
        this.accountFactory = accountFactory;
    }

    @Override
    protected AddAccountUseCase initialize() {
        return new AddAccountService(accountDSGateway.get(), accountFactory.get());
    }
}
