package com.sg.kata.application.port.in.soa;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.soa.Soa;

import java.time.LocalDateTime;
import java.util.Set;

public interface GetSOAUseCase {

    Set<Soa> getByAccountAndDate(AccountId accountNumber, LocalDateTime fromDate, LocalDateTime toDate);
}
