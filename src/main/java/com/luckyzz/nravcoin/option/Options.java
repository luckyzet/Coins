package com.luckyzz.nravcoin.option;

import com.akamecoder.api.Pathable;
import org.jetbrains.annotations.NotNull;

public enum Options implements Pathable {

    DATABASE_HOSTNAME("database.hostname"),
    DATABASE_PORT("database.port"),
    DATABASE_NAME("database.database"),
    DATABASE_USERNAME("database.username"),
    DATABASE_PASSWORD("database.password"),
    CACHE_SIZE("settings.cacheSize");

    private final String path;

    Options(@NotNull final String path) {
        this.path = path;
    }

    @Override
    public @NotNull String getPath() {
        return path;
    }

}
