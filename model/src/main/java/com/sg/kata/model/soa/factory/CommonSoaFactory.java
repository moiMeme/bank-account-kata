package com.sg.kata.model.soa.factory;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.soa.Soa;
import com.sg.kata.model.transaction.Transaction;

import java.util.Set;
import java.util.TreeSet;

public class CommonSoaFactory implements SoaFactory {

    @Override
    public Set<Soa> create(AccountId accountId, Set<Transaction> transactions) {
        Set<Soa> soaSet = new TreeSet<>();
        int lineNo = 0;
        for (Transaction trx : transactions) {
            soaSet.add(create(lineNo, accountId, trx));
            lineNo++;
        }
        return soaSet;
    }

    @Override
    public Soa create(int lineNo, AccountId accountId, Transaction trx) {
        Soa soa = new Soa();
        soa.setLineNo(lineNo);
        soa.setAccountId(accountId);
        soa.setTrsDate(trx.getTrsDate());
        soa.setTrsDescription(trx.getTrsType().getDescription());
        if (accountId.equals(trx.getCreditAccount())) {
            soa.setCreditAmount(trx.getAmount());
        } else {
            if (accountId.equals(trx.getDebitAccount())) {
                soa.setDebitAmount(trx.getAmount());
            } else {
                throw new CommonException(ErrorCode.EXP008);
            }
        }
        return soa;
    }
}
