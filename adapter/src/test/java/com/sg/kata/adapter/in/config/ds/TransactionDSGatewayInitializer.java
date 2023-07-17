package com.sg.kata.adapter.in.config.ds;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.adapter.out.transaction.InMemoryTransactionDSGateway;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;

public class TransactionDSGatewayInitializer extends LazyInitializer<TransactionDSGateway> {
    @Override
    protected TransactionDSGateway initialize() {
        return new InMemoryTransactionDSGateway();
    }
}
