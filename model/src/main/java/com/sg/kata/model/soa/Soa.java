package com.sg.kata.model.soa;

import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.validation.NotEmpty;
import com.sg.kata.model.common.validation.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Soa implements Comparable<Soa> {
    @NotNull
    private Integer lineNo;
    @NotNull
    private AccountId accountId;
    @NotNull
    private LocalDateTime trsDate;
    @NotEmpty
    private String trsDescription;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
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

    public String getTrsDescription() {
        return trsDescription;
    }

    public void setTrsDescription(String trsDescription) {
        this.trsDescription = trsDescription;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    @Override
    public int compareTo(Soa o) {
        return this.getLineNo() - o.getLineNo();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Soa soa = (Soa) o;
        return Objects.equals(lineNo, soa.lineNo) && Objects.equals(accountId, soa.accountId) && Objects.equals(trsDate, soa.trsDate) && Objects.equals(trsDescription, soa.trsDescription) && Objects.equals(creditAmount, soa.creditAmount) && Objects.equals(debitAmount, soa.debitAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNo, accountId, trsDate, trsDescription, creditAmount, debitAmount);
    }
}
