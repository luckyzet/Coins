package com.luckyzz.nravcoin.coin;

import com.akamecoder.api.Identifiable;
import com.luckyzz.nravcoin.language.LanguageConfig;
import com.luckyzz.nravcoin.language.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.LongUnaryOperator;

public class PlayerBalance implements Identifiable<String> {

    private final PlayerBalanceRepository repository;

    private final String identifier;
    private long coins;
    private long changed;

    PlayerBalance(@NotNull final PlayerBalanceRepository repository, @NotNull final String identifier, final long coins, final long changed) {
        this.repository = repository;
        this.identifier = identifier;
        this.coins = coins;
        this.changed = changed;
    }

    PlayerBalance(@NotNull final PlayerBalanceRepository repository, @NotNull final String identifier) {
        this(repository, identifier, 0, 0);
    }

    @Override
    public @NotNull String getIdentifier() {
        return identifier;
    }

    public long getCoins() {
        return coins;
    }

    public void modifyCoins(@NotNull final LongUnaryOperator operator) {
        final long old = coins;
        coins = operator.applyAsLong(coins);
        if(coins > old) {
            changed = changed + (coins - old);
        }
        repository.saveBalance(this);
    }

    public boolean isNotified() {
        return changed == 0;
    }

    public long getChanged() {
        return changed;
    }

    public void notifyPlayer(@NotNull final LanguageConfig language) {
        if (isNotified()) {
            return;
        }
        final Player player = Bukkit.getPlayerExact(identifier);
        if (player == null) {
            return;
        }
        if (changed > 0) {
            language.getAdaptiveMessage(Messages.NOTIFY_PLUS_BALANCE).placeholder("%coins%", changed).send(player);
        }

        changed = 0;
        repository.saveBalance(this);
    }

    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PlayerBalance that = (PlayerBalance) o;
        return new EqualsBuilder()
                .append(repository, that.repository)
                .append(coins, that.coins)
                .append(identifier, that.identifier)
                .append(changed, that.changed)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(coins)
                .append(changed)
                .append(repository)
                .toHashCode();
    }

    @Override
    public @NotNull String toString() {
        return "PlayerBalance{" +
                "identifier='" + identifier + '\'' +
                ", coins=" + coins +
                ", changed=" + changed +
                '}';
    }

}
