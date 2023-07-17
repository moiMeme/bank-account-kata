package com.sg.kata.application.port.out.account;

import com.sg.kata.application.port.out.common.DataSourceGatewayPort;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.transaction.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public interface AccountDSGateway extends DataSourceGatewayPort<Account, String> {

}
