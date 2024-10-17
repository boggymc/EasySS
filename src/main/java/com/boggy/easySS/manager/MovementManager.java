package com.boggy.easySS.manager;

import com.boggy.easySS.EasySS;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class MovementManager {
	private final HashMap<Player, Location> playerLastLocations;

	public MovementManager() {
		playerLastLocations = new HashMap<>();

		new BukkitRunnable() {
			public void run() {
				for (Player player : EasySS.instance.getFrozenPlayerManager().getFrozenPlayers()) {
					checkPlayerMovement(player);
				}
			}
		}.runTaskTimer(EasySS.instance, 0, 15);

	}

	public void checkPlayerMovement(Player player) {
		Location currentLocation = player.getLocation();
		Location previousLocation = playerLastLocations.get(player);

		if (previousLocation == null) {
			playerLastLocations.put(player, currentLocation);
			return;
		}

		if (!previousLocation.getWorld().equals(currentLocation.getWorld()) || previousLocation.distanceSquared(currentLocation) >= 1) {
			// Frozen player moved
			player.teleport(previousLocation);
		}
	}
}
