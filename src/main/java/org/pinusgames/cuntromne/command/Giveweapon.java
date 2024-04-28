package org.pinusgames.cuntromne.command;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.weapon.*;

public class Giveweapon implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) return false;
            ItemStack give = new ItemStack(Material.ACACIA_LEAVES);
            if(args[1].equals("ak")) give = Ak47.give(player);
            if(args[1].equals("deagle")) give = Deagle.give(player);
            if(args[1].equals("m4a1")) give = M4A1.give(player);
            if(args[1].equals("awp")) give = Awp.give(player);
            if(args[1].equals("glock")) give = Glock.give(player);
            if(args[1].equals("tester")) give = Tester.give(player);
            if(args[1].equals("he")) give = GrenadeHE.give(player);
            if(args[1].equals("flash")) give = GrenadeFlash.give(player);
            if(args[1].equals("smoke")) give = GrenadeSmoke.give(player);
            if(args[1].equals("usp")) give = USP.give(player);
            if(args[1].equals("c4")) give = C4.give(player);
            if(args[1].equals("p250")) give = P250.give(player);
            if(args[1].equals("tec9")) give = Tec9.give(player);
            if(args[1].equals("fakec4")) give = FakeC4.give(player);
            if(args[1].equals("defuse")) give = Defuse.give(player);
            if(args[1].equals("knife") && args.length > 2) {
                give = Knife.give(player, Integer.parseInt(args[2]));
            }
            player.getInventory().addItem(give);
            return true;
        }
        return false;
    }
}
