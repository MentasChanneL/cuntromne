package org.pinusgames.cuntromne.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.pinusgames.cuntromne.Cuntromne;

import java.io.File;

public class DonateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length > 2) {
            Player player = Bukkit.getPlayer(args[0]);
            if(player == null) return false;
            if(args[1].equals("knife")) {
                int cmd;
                try{
                    cmd = Integer.parseInt(args[2]);
                }catch (Exception e) {
                    return false;
                }
                File configFile = new File(Cuntromne.getInstance().getDataFolder(), "knifes.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

                config.set(player.getUniqueId().toString(), cmd);

                try {
                    config.save(configFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        }
        return false;
    }
}
