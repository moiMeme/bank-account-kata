package com.sg.kata.application.service.soa;

import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.Util;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import com.sg.kata.model.soa.Soa;
import com.sg.kata.model.soa.factory.CommonSoaFactory;
import com.sg.kata.model.soa.factory.SoaFactory;
import com.sg.kata.model.transaction.Transaction;
import com.sg.kata.model.transaction.TransactionId;
import com.sg.kata.model.transaction.TransactionType;
import com.sg.kata.model.transaction.factory.CommonTransactionFactory;
import com.sg.kata.model.transaction.factory.TransactionFactory;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class GetSOAServiceTest {
    private static final Currency EUR = Currency.getInstance("EUR");
    private static final AccountId TEST_ACCOUNT_ID = new AccountId("00000061157EUR123456");
    private static final TransactionFactory transactionFactory = new CommonTransactionFactory();

    private final SoaFactory soaFactory = new CommonSoaFactory();
    private final TransactionDSGateway transactionDSGateway = mock(TransactionDSGateway.class);
    private final GetSOAService getSOAService = new GetSOAService(transactionDSGateway, soaFactory);
    private final Set<Transaction> transactions = new TreeSet<>();
    private Set<Soa> statementOfAccount;

    @BeforeEach
    void initTest() {
        transactions.add(transactionFactory.create(new TransactionId(1), TEST_ACCOUNT_ID, new BigDecimal(150), TransactionType.DEPOSIT, EUR, LocalDateTime.of(2021, 1, 1, 0, 1)));
        transactions.add(transactionFactory.create(new TransactionId(2), TEST_ACCOUNT_ID, new BigDecimal(200), TransactionType.DEPOSIT, EUR, LocalDateTime.of(2021, 1, 1, 0, 2)));
        transactions.add(transactionFactory.create(new TransactionId(3), TEST_ACCOUNT_ID, new BigDecimal(50), TransactionType.WITHDRAWAL, EUR, LocalDateTime.of(2021, 1, 1, 0, 3)));

        statementOfAccount = soaFactory.create(TEST_ACCOUNT_ID, transactions);

        when(transactionDSGateway.findByAccountAndDate(any(), any(), any()))
                .thenReturn(transactions);
    }

    @Test
    void testGetByAccountAndDate() {
        LocalDateTime fromDate = LocalDateTime.of(2021, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        Set<Soa> result = getSOAService.getByAccountAndDate(TEST_ACCOUNT_ID, fromDate, toDate);
        assertThat(result).hasSize(3).containsAll(statementOfAccount);
        verify(transactionDSGateway, times(1)).findByAccountAndDate(any(), any(), any());
    }

    @Test
    void givenANewSearchForSOA_fromDateIsAfterToDate_ExceptionThrown() {
        LocalDateTime fromDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2021, 12, 31, 23, 59);
        ThrowableAssert.ThrowingCallable invocation = () -> getSOAService.getByAccountAndDate(TEST_ACCOUNT_ID, fromDate, toDate);
        String error = Util.formatErrorMessage(ErrorCode.EXP006, fromDate.format(DateTimeFormatter.ISO_DATE_TIME), toDate.format(DateTimeFormatter.ISO_DATE_TIME));
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(error);
        verify(transactionDSGateway, never()).save(any());
    }


}
