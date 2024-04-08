package org.pinusgames.cuntromne.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Round;

import java.util.Random;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length > 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) return false;
            if(args[1].equals("t")) Round.teamList.put( player.getUniqueId(), Cuntromne.getInstance().t );
            if(args[1].equals("ct")) Round.teamList.put( player.getUniqueId(), Cuntromne.getInstance().ct );
            System.out.println(Round.teamList);
            return true;
        }

        return false;
    }
}
