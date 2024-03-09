package com.stock.brocker.watchlist;

import com.stock.brocker.brockers.Symbol;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.serde.annotation.Serdeable;

import java.util.ArrayList;
import java.util.List;


@Serdeable
public record WatchList(List<Symbol> symbols) {

    public WatchList() {
        this(new ArrayList<>());
    }
}


