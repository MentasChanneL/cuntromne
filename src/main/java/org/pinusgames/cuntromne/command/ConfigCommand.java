package org.pinusgames.cuntromne.command;

import org.bukkit.block.BrewingStand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.Config;
import org.pinusgames.cuntromne.Cuntromne;

import java.io.File;
import java.io.IOException;

public class ConfigCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length > 0) {
            if(!(commandSender instanceof Player)) return false;
            Player player = (Player) commandSender;
            if(args[0].equals("lobby")) {
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "config.yml");
                Cuntromne.getInstance().getConfig().set("Lobby", player.getLocation());
                try {
                    Cuntromne.getInstance().getConfig().save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage("Lobby setted: " + player.getLocation());
                return true;
            }
            if(args[0].equals("login")) {
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "config.yml");
                Cuntromne.getInstance().getConfig().set("Login", player.getLocation());
                try {
                    Cuntromne.getInstance().getConfig().save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage("Login setted: " + player.getLocation());
                return true;
            }
            if(args[0].equals("t")) {
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "config.yml");
                Cuntromne.getInstance().getConfig().set("T_Spawn", player.getLocation());
                try {
                    Cuntromne.getInstance().getConfig().save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage("T_Spawn setted: " + player.getLocation());
                return true;
            }
            if(args[0].equals("ct")) {
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "config.yml");
                Cuntromne.getInstance().getConfig().set("CT_Spawn", player.getLocation());
                try {
                    Cuntromne.getInstance().getConfig().save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage("CT_Spawn setted: " + player.getLocation());
                return true;
            }
            if(args[0].equals("endgameentity") && args.length > 1) {
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "config.yml");
                Cuntromne.getInstance().getConfig().set("EndGameEntity", args[1]);
                try {
                    Cuntromne.getInstance().getConfig().save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage("EndGameEntity setted: " + args[1]);
                return true;
            }
            if(args[0].equals("plantA")) {
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "config.yml");
                Cuntromne.getInstance().getConfig().set( "PlantA", player.getLocation() );
                try {
                    Cuntromne.getInstance().getConfig().save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage("Plant A setted: " + player.getLocation());
                return true;
            }
            if(args[0].equals("plantB")) {
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "config.yml");
                Cuntromne.getInstance().getConfig().set( "PlantB", player.getLocation() );
                try {
                    Cuntromne.getInstance().getConfig().save(configFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                player.sendMessage("Plant B setted: " + player.getLocation());
                return true;
            }
            if(args[0].equals("!reload") || args[0].equals("reload")) {
                Config.instance( Cuntromne.getInstance() );
                commandSender.sendMessage("[o]");
                return true;
            }
        }

        return false;
    }
}
