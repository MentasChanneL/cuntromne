package org.pinusgames.cuntromne.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class exCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        if(args.length == 0) {
            Bukkit.broadcast(Component.text(player.getName()).color(TextColor.color( 255, 0, 200 ))
                    .append( Component.text(" хочет СЭКСА!").color(TextColor.color(255, 0, 120)))
            );
        }
        StringBuilder builder = new StringBuilder();
        for(String arg : args) {
            builder.append(arg);
        }
        Bukkit.broadcast(Component.text(player.getName()).color(TextColor.color( 255, 0, 200 ))
                .append( Component.text(" хочет СЭКСА с ").color(TextColor.color(255, 0, 120)))
                .append( Component.text(builder.toString()).color(TextColor.color( 255, 0, 200 ) ))
                .append( Component.text("!").color(TextColor.color(255, 0, 120)))
        );
        return true;
    }
}
