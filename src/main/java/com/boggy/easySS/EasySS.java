package com.boggy.easySS;

import com.boggy.easySS.command.PauseSsCommand;
import com.boggy.easySS.command.SsCommand;
import com.boggy.easySS.listener.ConnectionListener;
import com.boggy.easySS.manager.FrozenPlayerManager;
import com.boggy.easySS.manager.MovementManager;
import com.boggy.easySS.manager.PunishmentManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EasySS extends JavaPlugin {
	public static EasySS instance;
	private FrozenPlayerManager frozenPlayerManager;
	private PunishmentManager punishmentManager;
	private MovementManager movementManager;

	@Override
	public void onEnable() {
		instance = this;

		saveDefaultConfig();

		frozenPlayerManager = new FrozenPlayerManager();
		punishmentManager = new PunishmentManager();
		movementManager = new MovementManager();

		getCommand("ss").setExecutor(new SsCommand());
		getCommand("pausess").setExecutor(new PauseSsCommand());

		Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
	}

	@Override
	public void onDisable() {
		frozenPlayerManager.unFreezeAll();
	}

	public String getMessage(String key) {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString(key));
	}

	public FrozenPlayerManager getFrozenPlayerManager() {
		return frozenPlayerManager;
	}

	public PunishmentManager getPunishmentManager() {
		return punishmentManager;
	}
}
