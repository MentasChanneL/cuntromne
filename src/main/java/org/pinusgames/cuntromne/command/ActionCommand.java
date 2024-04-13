package org.pinusgames.cuntromne.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.actions.*;

public class ActionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length == 2) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) return false;
            Action action = null;
            if(args[1].equals("intro")) action = new IntroAction();
            if(args[1].equals("join")) action = new JoinAction();
            if(args[1].equals("lobby")) action = new LobbyAction();
            if(args[1].equals("lost")) action = new LostAction();
            if(args[1].equals("win")) action = new WinAction();
            if(action == null) return false;
            action.run(player);
        }

        return false;
    }
}
