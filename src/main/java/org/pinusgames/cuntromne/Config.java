package org.pinusgames.cuntromne;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class Config {

    public static Location lobby = null;
    public static Location login = null;
    public static Location tspawn = null;
    public static Location ctspawn = null;
    public static UUID endgameentity = null;

    public Config(Plugin plugin) {
        ConfigurationSection section = plugin.getConfig();
        lobby = section.getLocation("Lobby");
        login = section.getLocation("Login");
        tspawn = section.getLocation("T_Spawn");
        ctspawn = section.getLocation("CT_Spawn");
        try {
            endgameentity = UUID.fromString(section.getString("EndGameEntity"));
        }catch (Exception e) {
            endgameentity = null;
        }
    }

}
