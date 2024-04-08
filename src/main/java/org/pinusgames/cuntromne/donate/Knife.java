package org.pinusgames.cuntromne.donate;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.pinusgames.cuntromne.Cuntromne;

import java.io.File;
import java.util.UUID;

public class Knife {
    public static ItemStack getKnife(UUID player) {
        File configFile = new File(Cuntromne.getInstance().getDataFolder(), "knifes.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        int cmd;
        try {
            cmd = config.getInt(player.toString());
        }catch (Exception e) {
            return null;
        }
        if(cmd == 0) return null;
        return org.pinusgames.cuntromne.weapon.Knife.give(Bukkit.getPlayer(player), cmd);
    }
}
