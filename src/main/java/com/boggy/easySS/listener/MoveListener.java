package com.boggy.easySS.listener;

import com.boggy.easySS.EasySS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final EasySS plugin;

    public MoveListener(EasySS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (!plugin.getFrozenPlayerManager().isFrozen(player)) {
            return;
        }
        e.setCancelled(true);
    }
}
