package com.luckyzz.nravcoin.language;

import com.akamecoder.config.MessageConfig;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LanguageConfig extends MessageConfig<Messages> {

    public LanguageConfig(@NotNull final Plugin plugin) {
        System.out.println("Loading language config... (language.yml)");
        setup(builder -> builder.plugin(plugin).values(Messages.values()).name("language"));
        System.out.println("Loaded language config! (language.yml)");
    }

}
