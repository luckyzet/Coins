package com.luckyzz.nravcoin.coin;

import com.akamecoder.listener.Listener;
import com.luckyzz.nravcoin.language.LanguageConfig;
import com.luckyzz.nravcoin.language.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

class BalanceListener extends Listener {

    private static final BukkitScheduler scheduler = Bukkit.getScheduler();
    private final Plugin plugin;
    private final LanguageConfig language;
    private final PlayerBalanceCache cache;

    BalanceListener(@NotNull final Plugin plugin, @NotNull final LanguageConfig language, @NotNull final PlayerBalanceCache cache) {
        super(plugin);
        System.out.println("Registering balance listener...");
        this.plugin = plugin;
        this.language = language;
        this.cache = cache;
        System.out.println("Balance listener successfully registered!");
    }

    @EventHandler
    public void onCommandPreprocess(@NotNull final PlayerCommandPreprocessEvent event) {
        final String executed = event.getMessage();
        final String[] split = executed.split(" ");
        if (!executed.startsWith("/balance") && !executed.startsWith("/bal") && !executed.startsWith("/ebal")) {
            return;
        }
        final String name = split.length == 1 ? event.getPlayer().getName() : split[1];

        scheduler.runTask(plugin, () -> cache.getBalance(name).thenAccept(balance ->
                language.getAdaptiveMessage(Messages.BALANCE_STATE).placeholder("%coins%", balance.getCoins()).send(event.getPlayer())));
    }

    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        scheduler.runTask(plugin, () -> cache.getBalance(player.getName()).thenAccept(balance -> balance.notifyPlayer(language)));
    }

}
