package com.sg.kata.application.port.out.common;

import java.util.Optional;

public interface DataSourceGatewayPort<T, ID> {

    T save(T data);
    Optional<T> getById(ID id);

}
