package com.sg.kata.application.port.out.transaction;

import com.sg.kata.application.port.out.common.DataSourceGatewayPort;
import com.sg.kata.model.transaction.Transaction;

public interface TransactionDSGateway extends DataSourceGatewayPort<Transaction, Integer> {
}
