package com.luckyzz.nravcoin;

import com.luckyzz.nravcoin.coin.PlayerBalanceManager;
import com.luckyzz.nravcoin.language.LanguageConfig;
import com.luckyzz.nravcoin.option.OptionConfig;
import org.bukkit.plugin.java.JavaPlugin;

public final class NravCoinPlugin extends JavaPlugin {

    private PlayerBalanceManager balanceManager;

    @Override
    public void onEnable() {
        final OptionConfig option = new OptionConfig(this);
        final LanguageConfig language = new LanguageConfig(this);
        balanceManager = new PlayerBalanceManager(this, option, language);
    }

    @Override
    public void onDisable() {
        balanceManager.cancel();
    }

}
