package org.pinusgames.cuntromne.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.Team;
import org.pinusgames.cuntromne.weapon.*;

public class RoundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1) {
            if(args[0].equals("newgame")) {
                Round.newGame();
            }
            if(args[0].equals("stop")) {
                Round.timeLeft = 1;
                Round.prepareLeft = 0;
            }
            if(args[0].equals("endgameT")) {
                Round.timeLeft = 1;
                Round.prepareLeft = 0;
                Team.teamList.get("t").wins = 16;
            }
            if(args[0].equals("endgameCT")) {
                Round.timeLeft = 1;
                Round.prepareLeft = 0;
                Team.teamList.get("ct").wins = 16;
            }
            return true;
        }
        return false;
    }
}
