package com.luckyzz.nravcoin.coin;

import com.luckyzz.nravcoin.option.OptionConfig;
import com.luckyzz.nravcoin.option.Options;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PlayerBalanceCache {

    private final LoadingCache<String, CompletableFuture<PlayerBalance>> cache;

    PlayerBalanceCache(@NotNull final OptionConfig option, @NotNull final PlayerBalanceRepository repository) {
        System.out.println("Creating cache storage...");
        cache = CacheBuilder.newBuilder()
                .maximumSize(option.getLong(Options.CACHE_SIZE))
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, CompletableFuture<PlayerBalance>>() {
                    @Override
                    public @NotNull CompletableFuture<PlayerBalance> load(@NotNull final String identifier) {
                        return repository.loadBalance(identifier);
                    }
                });
        System.out.println("Cache storage successfully created!");
    }

    public @NotNull CompletableFuture<PlayerBalance> getBalance(@NotNull final String identifier) {
        try {
            return cache.get(identifier);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
