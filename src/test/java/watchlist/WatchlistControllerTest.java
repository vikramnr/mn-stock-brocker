package watchlist;


import com.stock.brocker.account.WatchlistController;
import com.stock.brocker.brockers.Symbol;
import com.stock.brocker.data.InMemoryAccountStore;
import com.stock.brocker.watchlist.WatchList;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class WatchlistControllerTest {


    @Inject
    @Client("/account/watchlist")
    HttpClient httpClient;

    public final static Logger LOG = LoggerFactory.getLogger(WatchlistControllerTest.class);
    private static final UUID TEST_ACCOUNT_ID = WatchlistController.ACCOUNT_ID;

    @Inject
    InMemoryAccountStore inMemoryTestAccountStore;

        @BeforeEach
    void setup() {
        inMemoryTestAccountStore.deleteWatchList(TEST_ACCOUNT_ID);
    }


    @Test
    void returnsEmptyWatchlistForTestAccount() {
        final WatchList result = httpClient.toBlocking().retrieve("/", WatchList.class);
        assertNull(result.symbols());
        assertTrue(inMemoryTestAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }

    WatchList createWatch(String[] testSymbols) {
        final List<Symbol> symbols = Stream.of(testSymbols)
                .map(Symbol::new)
                .collect(Collectors.toList());
        WatchList watchList = new WatchList(symbols);
        inMemoryTestAccountStore.updateWatchList(TEST_ACCOUNT_ID,watchList);
        return watchList;
    }

    @Test
    void returnsWatchListForTestAccount(){
        WatchList testWatchList = createWatch(new String[]{"APPL", "AMZN", "NFLX"});
        final WatchList response = httpClient.toBlocking().retrieve("/",WatchList.class);
        assertNotNull(response);
        assertEquals(testWatchList.symbols(),response.symbols());
    }

    @Test
    void updateWatchListForTestAccount() {
            WatchList testWatchList = createWatch(new String[]{"APPL", "ASXN", "NFLX"});
            final var request = HttpRequest.PUT("/", testWatchList);
            final HttpResponse<Object> response = httpClient.toBlocking().exchange(request);
            assertEquals(HttpStatus.OK, response.getStatus());
            assertEquals(testWatchList.symbols(),inMemoryTestAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols());
    }
    @Test
    void deleteWatchListForTestAccount() {
            WatchList testWatchList = createWatch(new String[]{"APPSL","ASNS","NFLX"});
            final var response = httpClient.toBlocking().exchange(HttpRequest.DELETE("/"));
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
            assertTrue(inMemoryTestAccountStore.getWatchList(TEST_ACCOUNT_ID).symbols().isEmpty());
    }
}
