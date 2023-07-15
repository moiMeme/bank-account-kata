package com.sg.kata.model.account;

import com.sg.kata.model.customer.CustomerId;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionFactoryTest;
import com.sg.kata.model.transaction.TransactionType;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static com.sg.kata.model.account.AccountFactoryTest.createAccountForCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class AccountTest {

    private final static CustomerId customerId = new CustomerId(12345);
    private final static Currency EUR = Currency.getInstance("EUR");

    @Test
    void givenANewAccount_addTwoTransaction_soaAreInAccount()  {
        Account account = createAccountForCustomer(customerId, EUR, AccountType.CLASSIC);

        Transaction transaction1 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(150), TransactionType.DEPOSIT, EUR);
        Transaction transaction2 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(160), TransactionType.WITHDRAWAL, EUR);

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);

        assertThat(account.getSoa()).hasSize(2);
    }

    @Test
    void givenANewAccount_addTwoTransactions_balanceIsCalculatedCorrectly() {
        Account account = createAccountForCustomer(customerId, EUR, AccountType.CLASSIC);

        Transaction transaction1 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(150), TransactionType.DEPOSIT, Currency.getInstance("EUR"));
        Transaction transaction2 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(160), TransactionType.WITHDRAWAL, Currency.getInstance("EUR"));

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);

        assertThat(account.getBalance().doubleValue()).isEqualTo(-10.00);
    }

    @Test
    void givenANewAccount_addTransactionForAnotherAccount_throwsException() {

        Account account = createAccountForCustomer(customerId, EUR, AccountType.CLASSIC);

        Account otherAccount = createAccountForCustomer(new CustomerId(55555), EUR, AccountType.CLASSIC);

        Transaction transaction = TransactionFactoryTest.createTransaction(otherAccount.getAccountNumber(), new BigDecimal(150), TransactionType.DEPOSIT, Currency.getInstance("EUR"));

        ThrowableAssert.ThrowingCallable invocation = () -> account.addTransaction(transaction);

        assertThatIllegalArgumentException()
                .isThrownBy(invocation)
                .satisfies(ex -> assertThat(ex.getMessage()).isEqualTo("Adding transaction not allowed for " + transaction.getTransactionId().value()));
    }


}
