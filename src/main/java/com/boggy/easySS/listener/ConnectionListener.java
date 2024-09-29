package com.boggy.easySS.listener;

import com.boggy.easySS.EasySS;
import com.boggy.easySS.Punishment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final EasySS plugin;

    public ConnectionListener(EasySS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (plugin.getFrozenPlayerManager().isFrozen(player)) {
            plugin.getFrozenPlayerManager().unFreezePlayer(player);
            plugin.getPunishmentManager().punishPlayer(player, Punishment.LEAVE);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().resetTitle();
        // Fixes the bug where the title will persist when
        // a player disconnects or gets kicked while frozen
    }
}
