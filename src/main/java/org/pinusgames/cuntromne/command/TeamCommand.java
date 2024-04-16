package org.pinusgames.cuntromne.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.Team;

import java.util.Random;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length > 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) return false;
            if(!Team.teamList.containsKey( args[1] )) {
                commandSender.sendMessage(Component.text("Команды " + args[1] + " не существует!").color(TextColor.color(255, 0, 0)));
                return false;
            }
            Team.teamList.get( args[1] ).addMember( player );
            commandSender.sendMessage("Игрок " + args[0] + " добавлен в команду " + args[1]);
            return true;
        }

        return false;
    }
}
