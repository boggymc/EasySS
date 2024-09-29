package com.boggy.easySS.manager;

import com.boggy.easySS.EasySS;
import com.boggy.easySS.Punishment;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class FrozenPlayerManager {

    private final EasySS plugin;

    private HashMap<UUID, Integer> frozenPlayers = new HashMap<>();
    private List<UUID> pausedPlayers = new ArrayList<>();

    public FrozenPlayerManager(EasySS plugin) {
        this.plugin = plugin;
    }

    public void freezePlayer(Player player) {
        addPlayer(player);
        player.setGameMode(GameMode.SPECTATOR);
        player.addPotionEffect(new PotionEffect(
                PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false, false));
        player.sendTitle(plugin.getMessage("title"), plugin.getMessage("subtitle"),0, 1000000000, 1000000000);
        chatReminder(player);
        if (plugin.getConfig().getBoolean("teleport-away")) {
            player.teleport(player.getLocation().add(0, 150, 0));
        }
    }

    public void unFreezePlayer(Player player) {
        removePlayer(player);
        player.resetTitle();
        player.setGameMode(GameMode.SURVIVAL);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        if (plugin.getConfig().getBoolean("teleport-away")) {
            player.teleport(player.getLocation().add(0, -150, 0));
        }
        unPauseFreeze(player);
    }

    public void pauseFreeze(Player player) {
        pausedPlayers.add(player.getUniqueId());
    }

    public void unPauseFreeze(Player player) {
        pausedPlayers.remove(player.getUniqueId());
    }

    private void chatReminder(Player player) {
        // This entire method is a bit of a mess - formatting messages with placeholders
        // AND a clickable link is not fun at all
        // todo : convert to adventure api colour formatting

        List<String> messages = plugin.getConfig().getStringList("frozen-chat-messages");
        List<TextComponent> components = new ArrayList<>();

        String delimiter = "%discord%";

        for (String string : messages) {

            if (string.contains(delimiter)) {
                string = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', string);
                String before = string.substring(0, string.indexOf(delimiter));
                String after = string.substring(string.indexOf(delimiter) + delimiter.length());

                TextComponent beforeComponent = new TextComponent(before);
                TextComponent discordComponent = new TextComponent(ChatColor.UNDERLINE + "Discord");
                discordComponent.setColor(ChatColor.getByChar(
                        plugin.getConfig().getString("discord-text-colour").replace("&", "")).asBungee());
                TextComponent afterComponent = new TextComponent(after);

                discordComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new Text("Click to join the discord")));

                discordComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                        plugin.getConfig().getString("discord-invite")));

                TextComponent component = beforeComponent;
                component.addExtra(discordComponent);
                component.addExtra(afterComponent);
                components.add(component);
            } else {
                components.add(new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', string)));
            }
        }

        Bukkit.getScheduler().runTaskTimer(plugin, (scheduler) -> {
            if (!frozenPlayers.containsKey(player.getUniqueId())) {
                scheduler.cancel();
                return;
            }
            if (frozenPlayers.get(player.getUniqueId()) == null) {
                scheduler.cancel();
                return;
            }
            int time = frozenPlayers.get(player.getUniqueId());
            if (pausedPlayers.contains(player.getUniqueId())) {
                return;
            }
            if (time <= 0) {
                plugin.getPunishmentManager().punishPlayer(player, Punishment.TIME);
                scheduler.cancel();
                return;
            }
            if (frozenPlayers.containsKey(player.getUniqueId())) {
                frozenPlayers.put(player.getUniqueId(), time - 1);
            }
            for (TextComponent component : components) {
                if (component.toPlainText().contains("%time_left%")) {
                    String formatted = component.toPlainText().replace("%time_left%",
                            frozenPlayers.get(player.getUniqueId()) + "");
                    player.sendMessage(formatted);
                    continue;
                }
                player.spigot().sendMessage(component);
            }
        }, 0L, 20L);
    }

    public void unFreezeAll() {
        for (UUID uuid : frozenPlayers.keySet()) {
            unFreezePlayer(Bukkit.getPlayer(uuid));
        }
    }

    private void addPlayer(Player player) { frozenPlayers.put(player.getUniqueId(), plugin.getConfig().getInt("time-limit")); }
    private void removePlayer(Player player) { frozenPlayers.remove(player.getUniqueId()); }

    public boolean isFrozen(Player player) { return frozenPlayers.containsKey(player.getUniqueId()); }
    public boolean isPaused(Player player) { return pausedPlayers.contains(player.getUniqueId()); }

}
