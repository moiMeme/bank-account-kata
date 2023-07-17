package com.sg.kata.application.service.account;

import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.account.AccountId;
import com.sg.kata.model.common.exception.CommonException;
import com.sg.kata.model.common.exception.ErrorCode;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.sg.kata.model.common.Util.formatErrorMessage;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GetBalanceServiceTest {

    private static final AccountId TEST_ACCOUNT_ID = new AccountId("00000061157EUR123456");
    private final TransactionDSGateway transactionDSGateway = mock(TransactionDSGateway .class);;
    private final GetBalanceService getBalanceService = new GetBalanceService(transactionDSGateway);


    @Test
    void testGetBalance() {
        LocalDateTime beforeThisDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        boolean inclusive = true;
        BigDecimal expectedBalance = new BigDecimal("100.00");

        when(transactionDSGateway.findBalanceByAccountAndDate(TEST_ACCOUNT_ID, beforeThisDate, inclusive))
                .thenReturn(expectedBalance);

        BigDecimal actualBalance = getBalanceService.getBalance(TEST_ACCOUNT_ID, beforeThisDate, inclusive);

        assertEquals(expectedBalance, actualBalance);
        verify(transactionDSGateway, times(1))
                .findBalanceByAccountAndDate(TEST_ACCOUNT_ID, beforeThisDate, inclusive);
    }

    @Test
    void testGetBalanceWithNullAccountId() {
        LocalDateTime beforeThisDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        boolean inclusive = true;

        ThrowableAssert.ThrowingCallable invocation = () -> getBalanceService.getBalance(null, beforeThisDate, inclusive);
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(formatErrorMessage(ErrorCode.EXP005, "accountId"));
        verify(transactionDSGateway, never()).findBalanceByAccountAndDate(any(), any(), anyBoolean());

    }

    @Test
    void testGetBalanceWithNullBeforeThisDate() {
        boolean inclusive = true;

        ThrowableAssert.ThrowingCallable invocation = () -> getBalanceService.getBalance(TEST_ACCOUNT_ID, null, inclusive);
        assertThatExceptionOfType(CommonException.class).isThrownBy(invocation).withMessage(formatErrorMessage(ErrorCode.EXP005, "beforeThisDate"));
        verify(transactionDSGateway, never()).findBalanceByAccountAndDate(any(), any(), anyBoolean());

    }


}
