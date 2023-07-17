package com.sg.kata.adapter.in.config.factory;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.model.account.factory.AccountFactory;
import com.sg.kata.model.account.factory.CommonAccountFactory;

public class AccountFactoryInitializer extends LazyInitializer<AccountFactory> {
    @Override
    protected AccountFactory initialize() {
        return new CommonAccountFactory();
    }
}
