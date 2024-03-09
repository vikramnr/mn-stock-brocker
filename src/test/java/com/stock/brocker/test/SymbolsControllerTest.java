package com.stock.brocker.test;

import com.stock.brocker.brockers.Symbol;
import com.stock.brocker.data.InMemoryStore;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.json.tree.JsonNode;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class SymbolsControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SymbolsControllerTest.class);
    @Inject
    @Client("/symbols")
    HttpClient httpClient;

    @Inject
    InMemoryStore inMemoryStore;

    @BeforeEach
    void setup() {
        inMemoryStore.initializeWith(10);
    }

    @Test
    void symbolsEndpointReturnsListofSymbol() {
        var response =  httpClient.toBlocking().exchange("/", JsonNode.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(10, response.getBody().get().size());
    }

    @Test
    void symbolEndpointReturnsTheCorrectSymbol() {
        var testSymbol = new Symbol("TEST");
        inMemoryStore.getSymbols().put(testSymbol.value(),testSymbol);
        var response = httpClient.toBlocking()
                        .exchange("/"+ testSymbol.value(), Symbol.class);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(testSymbol, response.getBody().get());
    }

    @Test
    void symbolsEndpointReturnsListofSymbolTakingIntoAccountQueryParameters() {
        var response =  httpClient.toBlocking().exchange("/filter?max=10", JsonNode.class);
        LOG.debug("Max:10 : {}",response.getBody().get().toString());
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(10, response.getBody().get().size());

        var offset =  httpClient.toBlocking().exchange("/filter?offset=7", JsonNode.class);
        LOG.debug("Max:10 : {}",offset.getBody().get().toString());
        assertEquals(HttpStatus.OK, offset.getStatus());
        assertEquals(3, offset.getBody().get().size());


        var max2Offset7 =  httpClient.toBlocking().exchange("/filter?max=2&offset=7", JsonNode.class);
        LOG.debug("Max:2 : {}",max2Offset7.getBody().get().toString());
        assertEquals(HttpStatus.OK, max2Offset7.getStatus());
        assertEquals(2, max2Offset7.getBody().get().size());
    }


}
