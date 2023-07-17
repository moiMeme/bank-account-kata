package com.sg.kata.adapter.in.config.factory;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.model.transaction.factory.CommonTransactionFactory;
import com.sg.kata.model.transaction.factory.TransactionFactory;

public class TransactionFactoryInitializer extends LazyInitializer<TransactionFactory> {
    @Override
    protected TransactionFactory initialize() {
        return new CommonTransactionFactory();
    }
}
