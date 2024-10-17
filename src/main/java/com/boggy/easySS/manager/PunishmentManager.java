package com.boggy.easySS.manager;

import com.boggy.easySS.EasySS;
import com.boggy.easySS.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PunishmentManager {

	private final EasySS plugin = EasySS.instance;

	public void punishPlayer(Player player, Punishment reason) {
		if (reason == Punishment.TIME && plugin.getConfig().getBoolean("on-time-limit.enabled")) {
			plugin.getFrozenPlayerManager().unFreezePlayer(player);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("on-time-limit.command")
					.replace("%player%", player.getName()));
		} else if (reason == Punishment.LEAVE && plugin.getConfig().getBoolean("on-leave.enabled")) {
			plugin.getFrozenPlayerManager().unFreezePlayer(player);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("on-leave.command")
					.replace("%player%", player.getName()));
		}
	}

}
