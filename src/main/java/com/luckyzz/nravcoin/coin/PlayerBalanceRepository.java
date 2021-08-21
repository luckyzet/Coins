package com.luckyzz.nravcoin.coin;

import com.akamecoder.api.Cancelable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface PlayerBalanceRepository extends Cancelable {

    @NotNull CompletableFuture<PlayerBalance> loadBalance(@NotNull final String identifier);

    void saveBalance(@NotNull final PlayerBalance balance);

}
