package com.sg.kata.application.port.out.soa;

import com.sg.kata.application.port.out.common.DataSourceGatewayPort;
import com.sg.kata.model.soa.StatementOfAccount;

import java.time.LocalDateTime;
import java.util.Set;

public interface SoaDSGateway extends DataSourceGatewayPort<StatementOfAccount, Integer> {

    Set<StatementOfAccount> findByAccountAndDate(String accountNumber, LocalDateTime fromDate, LocalDateTime toDate);
}
