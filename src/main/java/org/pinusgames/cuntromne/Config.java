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
    public static Location plantA = null;
    public static Location plantB = null;
    public static String tabTop = null;
    public static String tabBottom = null;

    public static void instance(Plugin plugin) {
        plugin.reloadConfig();
        ConfigurationSection section = plugin.getConfig();
        lobby = section.getLocation("Lobby");
        login = section.getLocation("Login");
        tspawn = section.getLocation("T_Spawn");
        ctspawn = section.getLocation("CT_Spawn");
        plantA = section.getLocation("PlantA");
        plantB = section.getLocation("PlantB");
        tabTop = section.getString("TablistHead");
        tabBottom = section.getString("TablistFoot");
        try {
            endgameentity = UUID.fromString(section.getString("EndGameEntity"));
        }catch (Exception e) {
            endgameentity = null;
        }
    }

}
