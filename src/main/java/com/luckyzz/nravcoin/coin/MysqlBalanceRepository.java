package com.luckyzz.nravcoin.coin;

import com.akamecoder.database.Database;
import com.akamecoder.database.DatabaseType;
import com.akamecoder.database.HikariDatabase;
import com.akamecoder.database.execute.DatabaseExecutor;
import com.akamecoder.database.execute.ExecutionType;
import com.luckyzz.nravcoin.option.OptionConfig;
import com.luckyzz.nravcoin.option.Options;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

class MysqlBalanceRepository implements PlayerBalanceRepository {

    private final HikariDatabase database;
    private final DatabaseExecutor executor;

    MysqlBalanceRepository(@NotNull final Plugin plugin, @NotNull final OptionConfig option) {
        System.out.println("Connecting to mysql database...");
        database = Database.createDatabase(builder -> builder.hikari().type(DatabaseType.MYSQL).plugin(plugin)
                .hostname(option.getOption(Options.DATABASE_HOSTNAME))
                .port((int) option.getOption(Options.DATABASE_PORT))
                .database(option.getOption(Options.DATABASE_NAME))
                .username(option.getOption(Options.DATABASE_USERNAME))
                .password(option.getOption(Options.DATABASE_PASSWORD)));
        executor = database.executor();
        System.out.println("Successfully connected to mysql database!");
        System.out.println("Creating mysql database structure...");
        executor.update(ExecutionType.SYNC, "CREATE TABLE IF NOT EXISTS `coins` (`identifier` VARCHAR(30) NOT NULL PRIMARY KEY, `coins` INT, `changed` INT);");
        System.out.println("Mysql database structure successfully created!");
    }

    @Override
    public @NotNull CompletableFuture<PlayerBalance> loadBalance(@NotNull final String identifier) {
        final CompletableFuture<PlayerBalance> future = new CompletableFuture<>();
        executor.result("SELECT * FROM coins WHERE identifier = ?;", result -> {
            if (!result.next()) {
                final PlayerBalance balance = new PlayerBalance(this, identifier);
                future.complete(balance);
                executor.update(ExecutionType.SYNC, "INSERT INTO coins VALUES (?, ?, ?);", balance.getIdentifier(), balance.getCoins(), balance.getChanged());
                return;
            }
            future.complete(new PlayerBalance(this, result.getString("identifier"),
                    result.getLong("coins"), result.getLong("changed")));
        }, identifier);
        return future;
    }

    @Override
    public void saveBalance(@NotNull final PlayerBalance balance) {
        executor.update("UPDATE coins SET `coins` = ?, `changed` = ? WHERE `identifier` = ?;", balance.getCoins(), balance.getChanged(), balance.getIdentifier());
    }

    @Override
    public void cancel() {
        try {
            System.out.println("Disconnecting mysql connection...");
            database.close();
            System.out.println("Successfully disconnected from mysql connection!");
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
