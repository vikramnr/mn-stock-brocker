package com.stock.brocker.data;

import com.stock.brocker.brockers.Symbol;
import com.stock.brocker.watchlist.WatchList;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Singleton
public class InMemoryAccountStore {


    private final HashMap<UUID, WatchList> watchListsPerAccount = new HashMap<>();

    public WatchList getWatchList(final UUID accountId) {
//        System.out.println(accountId);
        return watchListsPerAccount.getOrDefault(accountId, new  WatchList());
    }

    public WatchList updateWatchList(final UUID accountId,WatchList watchList){
        watchListsPerAccount.put(accountId,watchList);
//        System.out.println(watchListsPerAccount.get(accountId));
        return getWatchList(accountId);
    }

    public void deleteWatchList(final UUID accountId) {
        watchListsPerAccount.remove(accountId);
//        System.out.println(watchListsPerAccount.get(accountId));
    }
}
