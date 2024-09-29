package com.boggy.easySS.command;

import com.boggy.easySS.EasySS;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SsCommand implements CommandExecutor {
    private final EasySS plugin;

    public SsCommand(EasySS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (!player.hasPermission(plugin.getConfig().getString("ss-permission"))) {
            player.sendMessage(plugin.getMessage("no-permission"));
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(plugin.getMessage("incorrect-usage"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(plugin.getMessage("player-not-found"));
            return false;
        }

        if (plugin.getFrozenPlayerManager().isFrozen(player)) {
            plugin.getFrozenPlayerManager().unFreezePlayer(target);
        } else {
            plugin.getFrozenPlayerManager().freezePlayer(target);
        }

        return true;
    }
}
