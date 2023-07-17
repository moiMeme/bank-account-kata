package com.sg.kata.application.port.out.account;

import com.sg.kata.application.port.out.common.DataSourceGatewayPort;
import com.sg.kata.model.account.Account;
import com.sg.kata.model.account.AccountId;

public interface AccountDSGateway extends DataSourceGatewayPort<Account, AccountId> {
}
