package com.luckyzz.nravcoin.option;

import com.akamecoder.config.SettingConfig;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class OptionConfig extends SettingConfig<Options> {

    public OptionConfig(@NotNull final Plugin plugin) {
        System.out.println("Loading option config... (config.yml)");
        setup(builder -> builder.plugin(plugin).name("config").loader(got -> {
        }));
        System.out.println("Loaded option config! (config.yml)");
    }

    @Override
    public long getLong(@NotNull final Options path) {
        return super.getLong(path);
    }

}
