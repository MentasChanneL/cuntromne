package org.pinusgames.cuntromne.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.Shop;

public class CashCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 2) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) return false;
            Shop.money.put(player.getUniqueId(), Integer.parseInt(args[1]));
            commandSender.sendMessage("Cash " + args[1] + " for " + args[0]);
        }
        return false;
    }
}
