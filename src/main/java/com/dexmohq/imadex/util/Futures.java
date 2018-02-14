package com.dexmohq.imadex.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Futures {

    public static <T> CompletableFuture<T> completable(Future<? extends T> future) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
