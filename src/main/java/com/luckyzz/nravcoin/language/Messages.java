package com.luckyzz.nravcoin.language;

import com.akamecoder.api.Pathable;
import org.jetbrains.annotations.NotNull;

public enum Messages implements Pathable {

    BALANCE_STATE("balance.state"),
    NOTIFY_PLUS_BALANCE("notify.plusBalance"),
    ONLY_PLAYER("onlyPlayer"),
    NO_PERMISSION("noPermission"),
    GIVE_USAGE("giveCoin.usage"),
    GIVE_NOT_NUMBER("giveCoin.coinNotNumber"),
    GIVE_SUCCESS("giveCoin.success"),
    TAKE_USAGE("takeCoin.usage"),
    TAKE_NOT_NUMBER("takeCoin.coinNotNumber"),
    TAKE_NOT_ENOUGH("takeCoin.notEnough"),
    TAKE_SUCCESS("takeCoin.success");

    private final String path;

    Messages(@NotNull final String path) {
        this.path = path;
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }

}
