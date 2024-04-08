package org.pinusgames.cuntromne;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.pinusgames.cuntromne.command.*;
import org.pinusgames.cuntromne.command.tab.*;
import org.pinusgames.cuntromne.weapon.WeaponData;
import org.pinusgames.cuntromne.weapon.projectile.Projectile;
import org.pinusgames.cuntromne.weapon.script.Animations;

import java.util.*;

public final class Cuntromne extends JavaPlugin {

    public HashMap<Integer, WeaponData> weapons = new HashMap<>();
    public HashMap<UUID, Projectile> projectiles = new HashMap<>();
    public HashMap<UUID, Team> teamList = new HashMap<>();
    private static Cuntromne instance;
    private int WeaponID = 0;
    public final Set<Integer> tasks = new HashSet<>();
    public Config config;

    public Team ct;
    public Team t;

    @Override
    public void onEnable() {
        instance = this;
        this.config = new Config(this);
        this.saveDefaultConfig();
        this.saveResource("knifes.yml", false);
        Animations.initAnimates();
        this.ct = new Team("ct", "Контрононисты");
        this.t = new Team("t", "Экскремисты");
        Bukkit.getPluginManager().registerEvents(new Events(this), this);

        this.getCommand("giveweapon").setExecutor(new Giveweapon());
        this.getCommand("ctumteam").setExecutor(new TeamCommand());
        this.getCommand("action").setExecutor(new ActionCommand());
        this.getCommand("config").setExecutor(new ConfigCommand());
        this.getCommand("round").setExecutor(new RoundCommand());
        this.getCommand("cash").setExecutor(new CashCommand());
        this.getCommand("donate").setExecutor(new DonateCommand());

        this.getCommand("giveweapon").setTabCompleter(new Givewptab());
        this.getCommand("action").setTabCompleter(new ActionTab());
        this.getCommand("config").setTabCompleter(new ConfigTab());
        this.getCommand("round").setTabCompleter(new RoundTab());
        this.getCommand("donate").setTabCompleter(new DonateTab());
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
