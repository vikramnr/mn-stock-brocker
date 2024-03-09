package com.stock.brocker.account;


import com.stock.brocker.data.InMemoryAccountStore;
import com.stock.brocker.watchlist.WatchList;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;

import java.util.UUID;

@Controller("/account/watchlist")
public record WatchlistController(InMemoryAccountStore inMemoryAccountStore) {

    static final public UUID ACCOUNT_ID = UUID.randomUUID();
    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get() {
        return inMemoryAccountStore.getWatchList(ACCOUNT_ID);
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public WatchList update(@Body WatchList watchList){
        return inMemoryAccountStore.updateWatchList(ACCOUNT_ID,watchList);
    }

    @Status(HttpStatus.NO_CONTENT)
    @Delete(produces = MediaType.APPLICATION_JSON)
    public void delete(){
        inMemoryAccountStore.deleteWatchList(ACCOUNT_ID);
    }
}
