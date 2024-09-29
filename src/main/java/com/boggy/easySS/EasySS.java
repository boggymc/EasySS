package com.boggy.easySS;

import com.boggy.easySS.command.PauseSsCommand;
import com.boggy.easySS.command.SsCommand;
import com.boggy.easySS.listener.MoveListener;
import com.boggy.easySS.listener.ConnectionListener;
import com.boggy.easySS.manager.FrozenPlayerManager;
import com.boggy.easySS.manager.PunishmentManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EasySS extends JavaPlugin {
    private FrozenPlayerManager frozenPlayerManager;
    private PunishmentManager punishmentManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        frozenPlayerManager = new FrozenPlayerManager(this);
        punishmentManager = new PunishmentManager(this);

        getCommand("ss").setExecutor(new SsCommand(this));
        getCommand("pausess").setExecutor(new PauseSsCommand(this));

        Bukkit.getPluginManager().registerEvents(new MoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
    }

    @Override
    public void onDisable() {
        frozenPlayerManager.unFreezeAll();
    }

    public String getMessage(String key) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(key));
    }

    public FrozenPlayerManager getFrozenPlayerManager() { return frozenPlayerManager; }
    public PunishmentManager getPunishmentManager() { return punishmentManager; }
}
