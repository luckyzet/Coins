package com.luckyzz.nravcoin.coin;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface PlayerBalanceApi {

    @NotNull CompletableFuture<PlayerBalance> getBalance(@NotNull final String identifier);

    default @NotNull CompletableFuture<PlayerBalance> getBalance(@NotNull final Player player) {
        return getBalance(player.getName());
    }

}
