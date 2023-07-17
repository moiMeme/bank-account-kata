package com.sg.kata.adapter.in.config.factory;

import com.sg.kata.adapter.in.config.LazyInitializer;
import com.sg.kata.model.soa.factory.CommonSoaFactory;
import com.sg.kata.model.soa.factory.SoaFactory;

public class SoaFactoryInitializer extends LazyInitializer<SoaFactory> {
    @Override
    protected SoaFactory initialize() {
        return new CommonSoaFactory();
    }
}
