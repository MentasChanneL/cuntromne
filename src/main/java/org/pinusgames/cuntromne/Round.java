package org.pinusgames.cuntromne;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Round {

    public static int roundID = 0;
    public static BossBar bossbar = Bukkit.createBossBar("00:00", BarColor.WHITE, BarStyle.SOLID);

    public static int timeLeft;
    public static int prepareLeft;
    public static HashMap<UUID, Team> teamList = new HashMap<>();
    public static int schedulerTimer = 0;

    public static void newRound() {
        bossbar.setVisible(true);
        timeLeft = 2400;
        prepareLeft = 300;
        roundID++;
        for(int i = 0; i < bossbar.getPlayers().size(); i++) {
            bossbar.removePlayer( bossbar.getPlayers().get(i) );
        }
        for(UUID target : new HashSet<>( teamList.keySet() )) {
            Player player = Bukkit.getPlayer(target);
            if( player == null || !player.isValid() ) {teamList.remove(target); continue;}
            Team team = teamList.get(target);
            if(team.id.equals("ct")) {
                bossbar.addPlayer(player);
                player.teleport( Config.ctspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
                preparePlayer(player);
            }
            if(team.id.equals("t")) {
                bossbar.addPlayer(player);
                player.teleport( Config.tspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
                preparePlayer(player);
            }
        }
        schedulerTimer = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::tick, 1, 1 ).getTaskId();
    }

    private static void preparePlayer(Player player) {
        Team team = Round.teamList.get(player.getUniqueId());
        if(!player.getInventory().getItem(EquipmentSlot.CHEST).hasItemMeta()) {
            if(team.id.equals("ct")) {
                ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
                LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
                meta.setCustomModelData(2);
                meta.setColor(Color.fromRGB(50, 50, 250));
                armor.setItemMeta(meta);
                player.getInventory().setItem(EquipmentSlot.CHEST, armor);
            }
            if(team.id.equals("t")) {
                ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
                LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
                meta.setCustomModelData(2);
                meta.setColor(Color.fromRGB(250, 125, 0));
                armor.setItemMeta(meta);
                player.getInventory().setItem(EquipmentSlot.CHEST, armor);
            }
        }
        player.playSound(player.getLocation(), "ctum:round", 1, 1);
        player.getInventory().clear();
        player.setItemOnCursor(null);
        player.setHealth(20);
        player.setAbsorptionAmount(20);
        player.clearActivePotionEffects();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 250, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, PotionEffect.INFINITE_DURATION, 4, false, false));
    }

    private static void tick() {
        if(Round.prepareLeft > 0) {
            bossbar.setProgress((double) prepareLeft / 300);

            String display = String.valueOf(prepareLeft / 20 );
            if(display.length() < 2) display = "0" + display;
            bossbar.setTitle("00:" + display);

            Round.prepareLeft--;
            return;
        }
        if(Round.timeLeft > 0) {
            bossbar.setProgress((double) timeLeft / 2400);

            int seconds = timeLeft / 20;
            int minutes = seconds / 60;
            seconds = seconds - (minutes * 60);
            String sd = String.valueOf(seconds);
            String dm = String.valueOf(minutes);
            if(seconds < 10) sd = "0" + sd;
            if(minutes < 10) dm = "0" + dm;
            bossbar.setTitle(dm + ":" + sd);

            Round.timeLeft--;
            return;
        }
        Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
    }

}
