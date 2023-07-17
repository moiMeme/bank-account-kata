package com.sg.kata.adapter.in.config.service;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.adapter.in.config.ds.TransactionDSGatewayInitializer;
import com.sg.kata.adapter.in.config.factory.SoaFactoryInitializer;
import com.sg.kata.application.port.in.soa.GetSOAUseCase;
import com.sg.kata.application.service.soa.GetSOAService;

public class GetSOAServiceInitializer extends LazyInitializer<GetSOAUseCase> {

    private final TransactionDSGatewayInitializer transactionDSGateway;
    private final SoaFactoryInitializer soaFactory;

    public GetSOAServiceInitializer(TransactionDSGatewayInitializer transactionDSGateway, SoaFactoryInitializer soaFactory) {
        this.transactionDSGateway = transactionDSGateway;
        this.soaFactory = soaFactory;
    }

    @Override
    protected GetSOAUseCase initialize() {
        return new GetSOAService(transactionDSGateway.get(), soaFactory.get());
    }
}
