package com.sg.kata.model.account;

import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionFactoryTest;
import com.sg.kata.model.transaction.TransactionType;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.sg.kata.model.account.AccountFactoryTest.createAccountForRandomCustomer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class AccountTest {

    @Test
    void givenANewAccount_addTwoTransaction_soaAreInAccount()  {
        Account account = createAccountForRandomCustomer();

        Transaction transaction1 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(150), TransactionType.DEPOSIT);
        Transaction transaction2 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(160), TransactionType.WITHDRAWAL);

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);

        assertThat(account.getSoa()).hasSize(2);
    }

    @Test
    void givenANewAccount_addTwoTransactions_balanceIsCalculatedCorrectly() {
        Account account = createAccountForRandomCustomer();

        Transaction transaction1 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(150), TransactionType.DEPOSIT);
        Transaction transaction2 = TransactionFactoryTest.createTransaction(account.getAccountNumber(), new BigDecimal(160), TransactionType.WITHDRAWAL);

        account.addTransaction(transaction1);
        account.addTransaction(transaction2);

        assertThat(account.getBalance().doubleValue()).isEqualTo(-10.00);
    }

    @Test
    void givenANewAccount_addTransactionForAnotherAccount_throwsException() {

        Account account = createAccountForRandomCustomer();

        Account otherAccount = createAccountForRandomCustomer();

        Transaction transaction = TransactionFactoryTest.createTransaction(otherAccount.getAccountNumber(), new BigDecimal(150), TransactionType.DEPOSIT);

        ThrowableAssert.ThrowingCallable invocation = () -> account.addTransaction(transaction);

        assertThatIllegalArgumentException()
                .isThrownBy(invocation)
                .satisfies(ex -> assertThat(ex.getMessage()).isEqualTo("Adding transaction not allowed for " + transaction.getTransactionId().value()));
    }


}
