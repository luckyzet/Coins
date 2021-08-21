package com.luckyzz.nravcoin.coin;

import com.akamecoder.api.Cancelable;
import com.luckyzz.nravcoin.language.LanguageConfig;
import com.luckyzz.nravcoin.option.OptionConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class PlayerBalanceManager implements Cancelable, PlayerBalanceApi {

    private final PlayerBalanceRepository repository;
    private final PlayerBalanceCache cache;

    public PlayerBalanceManager(@NotNull final Plugin plugin, @NotNull final OptionConfig option, @NotNull final LanguageConfig language) {
        repository = new MysqlBalanceRepository(plugin, option);
        cache = new PlayerBalanceCache(option, repository);

        new BalanceListener(plugin, language, cache);

        System.out.println("Registering coin commands...");
        new GiveCoinCommand(language, this);
        new TakeCoinCommand(language, this);
        System.out.println("Successfully registered coin commands!");

        System.out.println("Registering player balance api...");
        Bukkit.getServicesManager().register(PlayerBalanceApi.class, this, plugin, ServicePriority.Highest);
        System.out.println("Successfully registered player balance api!");
    }

    @Override
    public @NotNull CompletableFuture<PlayerBalance> getBalance(@NotNull final String identifier) {
        return cache.getBalance(identifier);
    }

    public @NotNull PlayerBalanceCache getBalanceCache() {
        return cache;
    }

    @Override
    public void cancel() {
        repository.cancel();
    }

}
