package com.stock.brocker.brockers;


import io.micronaut.serde.annotation.SerdeImport;

@SerdeImport(Symbol.class)
public record Symbol(String value) {

}
