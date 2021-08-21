package com.luckyzz.nravcoin.coin;

import com.akamecoder.command.ChatCommand;
import com.akamecoder.command.ChatOperations;
import com.akamecoder.command.ChatType;
import com.luckyzz.nravcoin.language.LanguageConfig;
import com.luckyzz.nravcoin.language.Messages;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

class TakeCoinCommand extends ChatCommand {

    private final LanguageConfig language;
    private final PlayerBalanceManager balanceManager;

    TakeCoinCommand(@NotNull final LanguageConfig language, @NotNull final PlayerBalanceManager balanceManager) {
        super(builder -> builder.type(ChatType.COMMAND).label("takecoin"));
        this.language = language;
        this.balanceManager = balanceManager;
    }

    @Override
    public void execute(@NotNull final CommandSender sender, @NotNull final ChatOperations operations, @NotNull final String[] args) {
        if (!operations.permission(language.getMessage(Messages.NO_PERMISSION), "nravcoin.takecoin")) {
            return;
        }
        if (!operations.argumentsLength(2, language.getMessage(Messages.TAKE_USAGE))) {
            return;
        }
        final String identifier = args[0];
        if (!operations.messageIfFalse(NumberUtils.isNumber(args[1]), language.getMessage(Messages.TAKE_NOT_NUMBER))) {
            return;
        }
        final long coins = Long.parseLong(args[1]);
        balanceManager.getBalance(identifier).thenAccept(balance -> {
            if (!operations.messageIfFalse(balance.getCoins() >= coins, language.getMessage(Messages.TAKE_NOT_ENOUGH))) {
                return;
            }
            balance.modifyCoins(got -> got - coins);
            balance.notifyPlayer(language);
            language.getMessage(Messages.TAKE_SUCCESS).send(sender);
        });
    }

}
