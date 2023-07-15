package com.sg.kata.model.soa;

import com.sg.kata.model.account.AccountId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class StatementOfAccount implements Comparable<StatementOfAccount> {

    private int lineNo;
    private AccountId accountId;
    private LocalDateTime trsDate;
    private String Description;
    private BigDecimal amount;

    public StatementOfAccount(AccountId accountId, LocalDateTime trsDate, String description, BigDecimal amount) {
        this.accountId = accountId;
        this.trsDate = trsDate;
        Description = description;
        this.amount = amount;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public void setAccountId(AccountId accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getTrsDate() {
        return trsDate;
    }

    public void setTrsDate(LocalDateTime trsDate) {
        this.trsDate = trsDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(StatementOfAccount soa) {
        return this.trsDate.compareTo(soa.getTrsDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatementOfAccount that = (StatementOfAccount) o;
        return lineNo == that.lineNo && Objects.equals(accountId, that.accountId) && Objects.equals(trsDate, that.trsDate) && Objects.equals(Description, that.Description) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNo, accountId, trsDate, Description, amount);
    }
}
