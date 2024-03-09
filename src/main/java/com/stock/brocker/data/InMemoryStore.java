package com.stock.brocker.data;

import com.stock.brocker.brockers.Symbol;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Singleton
public class InMemoryStore {

    private final Logger LOG = LoggerFactory.getLogger(InMemoryStore.class);
    private final Map<String, Symbol> symbols = new HashMap<>();

    private final Faker faker = new Faker();

    @PostConstruct
    public void initialize() {
        initializeWith(10);
    }


    public void initializeWith(int numberOfEntries) {
        symbols.clear();
        IntStream.range(0,numberOfEntries).forEach(i -> addNewSymbol());
    }


    private void addNewSymbol() {
        var symbol = new Symbol(faker.stock().nsdqSymbol());
        symbols.put(symbol.value(),symbol);
        LOG.debug("Symbol added {symbol}",symbol);
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }
}
