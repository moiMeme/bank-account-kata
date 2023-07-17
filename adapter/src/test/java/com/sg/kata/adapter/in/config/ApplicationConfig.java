package com.sg.kata.adapter.in.config;

import com.sg.kata.adapter.in.config.ds.AccountDSGatewayInitializer;
import com.sg.kata.adapter.in.config.ds.TransactionDSGatewayInitializer;
import com.sg.kata.adapter.in.config.factory.AccountFactoryInitializer;
import com.sg.kata.adapter.in.config.factory.SoaFactoryInitializer;
import com.sg.kata.adapter.in.config.factory.TransactionFactoryInitializer;
import com.sg.kata.adapter.in.config.service.AddAccountServiceInitializer;
import com.sg.kata.adapter.in.config.service.CreateTransactionServiceInitializer;
import com.sg.kata.adapter.in.config.service.GetBalanceServiceInitializer;
import com.sg.kata.adapter.in.config.service.GetSOAServiceInitializer;
import com.sg.kata.application.port.in.account.AddAccountUseCase;
import com.sg.kata.application.port.in.account.GetBalanceUserCase;
import com.sg.kata.application.port.in.soa.GetSOAUseCase;
import com.sg.kata.application.port.in.transaction.CreateTransactionUseCase;
import com.sg.kata.application.port.out.account.AccountDSGateway;
import com.sg.kata.application.port.out.transaction.TransactionDSGateway;
import com.sg.kata.model.transaction.TransactionType;

import java.util.Currency;

public class ApplicationConfig {

    private static final AccountFactoryInitializer accountFactoryInitializer = new AccountFactoryInitializer();
    private static final TransactionFactoryInitializer transactionFactoryInitializer = new TransactionFactoryInitializer();
    private static final SoaFactoryInitializer soaFactoryInitializer = new SoaFactoryInitializer();
    private static final AccountDSGatewayInitializer accountDSGatewayInitializer = new AccountDSGatewayInitializer();
    private static final TransactionDSGatewayInitializer transactionDSGatewayInitializer = new TransactionDSGatewayInitializer();
    private static final AddAccountServiceInitializer addAccountServiceInitializer = new AddAccountServiceInitializer(accountDSGatewayInitializer, accountFactoryInitializer);
    private static final CreateTransactionServiceInitializer createTransactionServiceInitializer = new CreateTransactionServiceInitializer(accountDSGatewayInitializer, transactionDSGatewayInitializer, transactionFactoryInitializer);
    private static final GetBalanceServiceInitializer getBalanceServiceInitializer = new GetBalanceServiceInitializer(transactionDSGatewayInitializer);
    private static final GetSOAServiceInitializer getSOAServiceInitializer = new GetSOAServiceInitializer(transactionDSGatewayInitializer, soaFactoryInitializer);

    public static AddAccountUseCase getAddAccountUseCase() {
        return addAccountServiceInitializer.get();
    }

    public static CreateTransactionUseCase getCreateTransactionUseCase() {
        return createTransactionServiceInitializer.get();
    }

    public static GetBalanceUserCase getGetBalanceUserCase() {
        return getBalanceServiceInitializer.get();
    }

    public static GetSOAUseCase getGetSOAUseCase() {
        return getSOAServiceInitializer.get();
    }

    public static TransactionDSGateway getTransactionDSGateway() {
        return transactionDSGatewayInitializer.get();
    }

    public static AccountDSGateway getAccountDSGateway() {
        return accountDSGatewayInitializer.get();
    }

    public static void createInternalAccount() {
        for (TransactionType transactionType : TransactionType.values()) {
            getAccountDSGateway().save(accountFactoryInitializer.get().create(Currency.getInstance("EUR"), transactionType));
        }
    }


}
