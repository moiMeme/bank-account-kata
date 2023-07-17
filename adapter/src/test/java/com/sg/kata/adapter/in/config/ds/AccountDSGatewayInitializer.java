package com.sg.kata.adapter.in.config.ds;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.adapter.out.account.InMemoryAccountDSGateway;
import com.sg.kata.application.port.out.account.AccountDSGateway;

public class AccountDSGatewayInitializer extends LazyInitializer<AccountDSGateway> {
    @Override
    protected AccountDSGateway initialize() {
        return new InMemoryAccountDSGateway();
    }
}
