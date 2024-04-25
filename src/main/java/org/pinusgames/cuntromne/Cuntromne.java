package org.pinusgames.cuntromne;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.pinusgames.cuntromne.command.*;
import org.pinusgames.cuntromne.command.tab.*;
import org.pinusgames.cuntromne.weapon.WeaponData;
import org.pinusgames.cuntromne.weapon.projectile.Projectile;
import org.pinusgames.cuntromne.weapon.script.Animations;

import java.util.*;

public final class Cuntromne extends JavaPlugin {

    public HashMap<Integer, WeaponData> weapons = new HashMap<>();
    public HashMap<UUID, Projectile> projectiles = new HashMap<>();
    private static Cuntromne instance;
    private int WeaponID = 0;

    @Override
    public void onEnable() {
        instance = this;
        Config.instance( Cuntromne.getInstance() );
        this.saveDefaultConfig();
        this.saveResource("knifes.yml", false);
        Animations.initAnimates();
        Shop.initialize();
        TextFormatter.init();
        new Team("ct", "Контрононисты", TextColor.color(0, 50, 255), ChatColor.BLUE);
        new Team("t", "Экскремисты", TextColor.color(255, 128, 0), ChatColor.GOLD);
        new Team("spectator", "Привидения с мотором", TextColor.color(255, 255, 255), ChatColor.WHITE);
        Bukkit.getPluginManager().registerEvents(new Events(this), this);
        Bukkit.getServer().resetRecipes();

        this.getCommand("giveweapon").setExecutor(new Giveweapon());
        this.getCommand("ctumteam").setExecutor(new TeamCommand());
        this.getCommand("action").setExecutor(new ActionCommand());
        this.getCommand("config").setExecutor(new ConfigCommand());
        this.getCommand("round").setExecutor(new RoundCommand());
        this.getCommand("cash").setExecutor(new CashCommand());
        this.getCommand("donate").setExecutor(new DonateCommand());
        this.getCommand("sex").setExecutor(new exCommand());

        this.getCommand("giveweapon").setTabCompleter(new Givewptab());
        this.getCommand("action").setTabCompleter(new ActionTab());
        this.getCommand("config").setTabCompleter(new ConfigTab());
        this.getCommand("round").setTabCompleter(new RoundTab());
        this.getCommand("donate").setTabCompleter(new DonateTab());
        this.getCommand("ctumteam").setTabCompleter(new TeamTab());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public int getWeaponID() {
        this.WeaponID++;
        return this.WeaponID;
    }

    public static Cuntromne getInstance() { return instance; }

}
